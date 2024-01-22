mod action;
mod notify_target;

use crate::action::Action;
use crate::notify_target::NotifyTarget;
use entity::guild_preferences::{ActiveModel, Entity};
use eyre::{Error, OptionExt, Result, WrapErr};
use migration::{Migrator, MigratorTrait};
use poise::{Framework, FrameworkOptions};
use sea_orm::ActiveValue::Set;
use sea_orm::{ActiveModelTrait, ConnectOptions, Database, DatabaseConnection, EntityTrait};
use serenity::all::{ChannelId, Context, GuildChannel};
use serenity::builder::{CreateEmbed, CreateEmbedAuthor, CreateEmbedFooter, CreateMessage};
use serenity::client::FullEvent;
use serenity::http::CacheHttp;
use serenity::model::Timestamp;
use serenity::prelude::{GatewayIntents, Mentionable};
use serenity::Client;
use std::env;
use tracing::log;

struct Data {
    db: DatabaseConnection,
}

#[tokio::main]
async fn main() -> Result<()> {
    tracing_subscriber::fmt()
        .with_max_level(tracing::Level::DEBUG)
        .init();
    dotenvy::dotenv()?;

    let mut opt = ConnectOptions::new("sqlite://notify.db?mode=rwc");
    opt.sqlx_logging(false)
        .sqlx_logging_level(log::LevelFilter::Info);
    let db = Database::connect(opt).await?;
    Migrator::up(&db, None).await?;

    let token = env::var("DISCORD_TOKEN").wrap_err("DISCORD_TOKEN not found in .env file")?;
    let intents = GatewayIntents::non_privileged()
        | GatewayIntents::GUILDS
        | GatewayIntents::GUILD_VOICE_STATES
        | GatewayIntents::GUILD_MEMBERS;

    let framework = Framework::<Data, Error>::builder()
        .setup(|ctx, _ready, framework| {
            Box::pin(async move {
                poise::builtins::register_globally(ctx, &framework.options().commands).await?;
                Ok(Data { db })
            })
        })
        .options(FrameworkOptions {
            commands: vec![notify()],
            event_handler: |ctx, event, framework, data| {
                Box::pin(event_handler(ctx, event, framework, data))
            },
            ..FrameworkOptions::default()
        })
        .build();

    let mut client = Client::builder(token, intents).framework(framework).await?;
    client.start().await?;
    Ok(())
}

#[poise::command(slash_command)]
async fn notify(
    ctx: poise::Context<'_, Data, Error>,
    #[description = "The notification target"] target: NotifyTarget,
) -> Result<()> {
    let voice_channel = ctx
        .guild()
        .ok_or_eyre("Not inside a guild")?
        .voice_states
        .get(&ctx.author().id)
        .and_then(|voice_state| voice_state.channel_id);

    match voice_channel {
        Some(_) => {
            ctx.say(format!(
                "{}, {} is in {}",
                target.mention(),
                ctx.author().mention(),
                ctx.channel_id().mention()
            ))
            .await?;
        }
        None => {
            ctx.defer_ephemeral().await?;
            ctx.reply(format!(
                "{}, you must be in a voice channel to use this command.",
                ctx.author().mention()
            ))
            .await?;
        }
    };

    Ok(())
}

async fn event_handler(
    ctx: &Context,
    event: &FullEvent,
    _framework: poise::FrameworkContext<'_, Data, Error>,
    data: &Data,
) -> Result<()> {
    match event {
        FullEvent::VoiceStateUpdate { old, new } => {
            let member = match &new.member {
                Some(member) => {
                    if member.user.bot {
                        return Ok(());
                    }
                    member
                }
                None => return Ok(()),
            };

            let action = match (
                old.as_ref().and_then(|old| old.channel_id),
                new.channel_id,
                old.as_ref().and_then(|old| old.self_stream),
                new.self_stream,
                old.as_ref().map(|old| old.self_video),
                new.self_video,
            ) {
                (None, _, _, _, _, _) => Action::Join,
                (Some(old_channel), Some(new_channel), _, _, _, _)
                    if old_channel != new_channel =>
                {
                    Action::Switch
                }
                (_, None, _, _, _, _) => Action::Leave,
                (_, _, None, Some(true), _, _) => Action::Stream,
                (_, _, _, _, Some(false), true) => Action::Video,
                _ => return Ok(()),
            };

            let send_channel = Entity::find_by_id(new.guild_id.map(|id| id.get() as i64).unwrap())
                .one(&data.db)
                .await?
                .and_then(|model| model.channel_id)
                .map(|id| ChannelId::new(id as u64))
                .or(new.channel_id)
                .or(old.as_ref().and_then(|old| old.channel_id));

            let channel_mention = if action == Action::Leave {
                old.as_ref().and_then(|old| old.channel_id)
            } else {
                new.channel_id
            };

            let mut embed_author = CreateEmbedAuthor::new(member.display_name());
            if let Some(avatar_url) = member.avatar_url() {
                embed_author = embed_author.icon_url(avatar_url);
            }

            let embed = CreateEmbed::new()
                .title(format!(
                    "{} {} {}",
                    member.display_name(),
                    action.as_phrase(),
                    channel_mention
                        .unwrap()
                        .name(&ctx.http)
                        .await
                        .unwrap_or_default()
                ))
                .timestamp(Timestamp::now())
                .author(embed_author)
                .footer(CreateEmbedFooter::new(action.as_emoji()));
            let message = CreateMessage::new().embed(embed);
            let _ = send_channel
                .unwrap()
                .send_message((&ctx.cache, ctx.http()), message)
                .await;
        }
        FullEvent::GuildCreate { guild, is_new: _ } => {
            let is_new_guild = Entity::find_by_id(guild.id.get() as i64)
                .one(&data.db)
                .await?
                .is_none();
            if is_new_guild {
                let default_channel = guild.default_channel(ctx.cache.current_user().id);
                let new_guild = ActiveModel {
                    guild_id: Set(guild.id.get() as i64),
                    channel_id: Set(default_channel.map(|channel| channel.id.get() as i64)),
                    ..Default::default()
                };
                let _insert = Entity::insert(new_guild).exec(&data.db).await?;
                if let Some(channel) = default_channel {
                    channel.send_message(
                        (&ctx.cache, ctx.http()),
                        CreateMessage::new()
                            .content(format!("Thanks for inviting me! For now, I have set {} as the text channel for voice state notifications. This can be configured with `/configure channel`.", channel.mention())),
                    ).await?;
                }
            }
        }
        FullEvent::GuildDelete {
            incomplete,
            full: _,
        } => {
            if !incomplete.unavailable {
                Entity::delete_by_id(incomplete.id.get() as i64)
                    .exec(&data.db)
                    .await?;
            }
        }
        FullEvent::ChannelCreate { channel } => {
            let guild_in_db = Entity::find_by_id(channel.guild_id.get() as i64)
                .one(&data.db)
                .await?;
            if let Some(guild) = guild_in_db {
                if guild.channel_id.is_none() {
                    let mut active_model: ActiveModel = guild.into();
                    active_model.channel_id = Set(Some(channel.id.get() as i64));
                    active_model.update(&data.db).await?;
                }
            }
        }
        FullEvent::ChannelDelete {
            channel,
            messages: _,
        } => {
            let all_channels = channel.guild_id.channels(ctx.http()).await?;
            let guild_in_db = Entity::find_by_id(channel.guild_id.get() as i64)
                .one(&data.db)
                .await?;
            if all_channels
                .values()
                .filter(|guild_channel| guild_channel.is_text_based())
                .count()
                == 0
            {
                let mut guild: ActiveModel = guild_in_db.unwrap().into();
                guild.channel_id = Set(None);
                guild.update(&data.db).await?;
            } else if guild_in_db
                .as_ref()
                .and_then(|model| model.channel_id)
                .map(|id| ChannelId::new(id as u64))
                == Some(channel.id)
            {
                let mut guild: ActiveModel = guild_in_db.unwrap().into();
                guild.channel_id = Set(all_channels
                    .into_values()
                    .find(GuildChannel::is_text_based)
                    .map(|channel| channel.id.get() as i64));
                guild.update(&data.db).await?;
            }
        }
        _ => {}
    }
    Ok(())
}
