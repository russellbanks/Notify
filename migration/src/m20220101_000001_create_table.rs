use sea_orm_migration::prelude::*;

#[derive(DeriveMigrationName)]
pub struct Migration;

#[async_trait::async_trait]
impl MigrationTrait for Migration {
    async fn up(&self, manager: &SchemaManager) -> Result<(), DbErr> {
        manager
            .create_table(
                Table::create()
                    .table(GuildPreferences::Table)
                    .if_not_exists()
                    .col(
                        ColumnDef::new(GuildPreferences::GuildId)
                            .big_unsigned()
                            .not_null()
                            .primary_key(),
                    )
                    .col(ColumnDef::new(GuildPreferences::ChannelId).big_unsigned())
                    .col(
                        ColumnDef::new(GuildPreferences::Join)
                            .boolean()
                            .not_null()
                            .default(true),
                    )
                    .col(
                        ColumnDef::new(GuildPreferences::Switch)
                            .boolean()
                            .not_null()
                            .default(true),
                    )
                    .col(
                        ColumnDef::new(GuildPreferences::Leave)
                            .boolean()
                            .not_null()
                            .default(true),
                    )
                    .col(
                        ColumnDef::new(GuildPreferences::Stream)
                            .boolean()
                            .not_null()
                            .default(true),
                    )
                    .col(
                        ColumnDef::new(GuildPreferences::Video)
                            .boolean()
                            .not_null()
                            .default(true),
                    )
                    .to_owned(),
            )
            .await
    }

    async fn down(&self, manager: &SchemaManager) -> Result<(), DbErr> {
        manager
            .drop_table(Table::drop().table(GuildPreferences::Table).to_owned())
            .await
    }
}

#[derive(DeriveIden)]
enum GuildPreferences {
    Table,
    GuildId,
    ChannelId,
    Join,
    Switch,
    Leave,
    Stream,
    Video,
}
