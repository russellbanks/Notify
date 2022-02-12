package db

import vcStateChange.Action

data class GuildPrefs(
    val guildId: String,
    val channelId: String,
    val join: Boolean,
    val switch: Boolean,
    val leave: Boolean,
    val stream: Boolean,
    val video: Boolean
) {

    fun getDisabled() = mutableListOf<String>().apply {
        if (!join) add(Action.JOIN.name.lowercase())
        if (!switch) add(Action.SWITCH.name.lowercase())
        if (!leave) add(Action.LEAVE.name.lowercase())
        if (!stream) add(Action.STREAM.name.lowercase())
        if (!video) add(Action.VIDEO.name.lowercase())
    }

}