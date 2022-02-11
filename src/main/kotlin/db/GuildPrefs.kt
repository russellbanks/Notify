package db
data class GuildPrefs(
    val guildId: String,
    val channelId: String,
    val join: Boolean,
    val switch: Boolean,
    val leave: Boolean,
    val stream: Boolean,
    val video: Boolean
) {

    fun getDisabled(): MutableList<String> {
        val list = mutableListOf<String>()
        if (!join) list.add("join")
        if (!switch) list.add("switch")
        if (!leave) list.add("leave")
        if (!stream) list.add("stream")
        if (!video) list.add("video")
        return list
    }

}