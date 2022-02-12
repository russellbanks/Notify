package vcStateChange

import bot
import data.Datastore
import dev.kord.common.Color
import dev.kord.common.entity.Snowflake
import dev.kord.core.behavior.channel.MessageChannelBehavior
import dev.kord.core.behavior.channel.createEmbed
import dev.kord.core.event.user.VoiceStateUpdateEvent
import kotlinx.datetime.Clock

suspend fun voiceStateChange(event: VoiceStateUpdateEvent): Error? {

    val action = when {
        (event.old?.getChannelOrNull() == null) -> Action.JOIN
        (event.old?.getChannelOrNull() != event.state.getChannelOrNull() && event.state.getChannelOrNull() != null) -> Action.SWITCH
        (event.state.getChannelOrNull() == null) -> Action.LEAVE
        (event.old?.isSelfSteaming == false && event.state.isSelfSteaming) -> Action.STREAM
        else -> throw Error("Unknown action")
    }

    val member = event.state.getMemberOrNull() ?: throw Error("Member is null")
    val channelId = if (action == Action.LEAVE) event.old?.channelId!! else event.state.channelId!!
    val channel = bot.client.getChannel(channelId) ?: throw Error("Channel is null")

    if (member.isBot) throw Error("Member is a bot")

    val prefs = Datastore.GuildPrefsCollection.get(channel.data.guildId.value?.value.toString())

    for (feature in prefs.getDisabled()) {
        if (feature.uppercase() == action.name) {
            throw Error("${action.name.lowercase().replaceFirstChar { it.titlecase() }} blocked by config")
        }
    }

    MessageChannelBehavior(Snowflake(prefs.channelId), bot.client).createEmbed {
        color = Color(0x00, 0x67, 0xf4)
        title = "${member.displayName} ${action.text} ${channel.data.name.value}"
        timestamp = Clock.System.now()
        author {
            name = member.displayName
            icon = member.avatar?.url
        }
        footer {
            text = action.emojiUnicode
        }
    }

    return null
}