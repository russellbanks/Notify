package db
data class GuildPrefs(
    val guildId: String,
    val channelId: String,
    val join: Boolean,
    val switch: Boolean,
    val leave: Boolean,
    val stream: Boolean,
    val video: Boolean
)