package vcStateChange

import bot
import data.Datastore
import data.GuildPrefs
import dev.kord.common.Color
import dev.kord.common.entity.Snowflake
import dev.kord.core.behavior.channel.MessageChannelBehavior
import dev.kord.core.behavior.channel.createEmbed
import dev.kord.core.entity.channel.Channel
import dev.kord.core.event.user.VoiceStateUpdateEvent
import io.klogging.Klogging
import kotlinx.datetime.Clock

object StateChange: Klogging {
    suspend fun voiceStateChange(event: VoiceStateUpdateEvent) {

        val member = event.state.getMemberOrNull() ?: return
        if (member.isBot) return

        val action = when {
            (event.old?.getChannelOrNull() == null) -> Action.JOIN
            (event.old?.getChannelOrNull() != event.state.getChannelOrNull() && event.state.getChannelOrNull() != null) -> Action.SWITCH
            (event.state.getChannelOrNull() == null) -> Action.LEAVE
            (event.old?.isSelfSteaming == false && event.state.isSelfSteaming) -> Action.STREAM
            else -> return
        }

        val channelId = if (action == Action.LEAVE) event.old?.channelId!! else event.state.channelId!!
        val channel = bot.client.getChannel(channelId) ?: return

        val prefs = getPrefs(channel)

        logBlockedPrefs(prefs, action)

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
    }

    private suspend fun logBlockedPrefs(prefs: GuildPrefs, action: Action) {
        for (feature in prefs.getDisabled()) {
            if (feature.uppercase() == action.name) {
                logger.warn("${action.name.lowercase().replaceFirstChar { it.titlecase() }} blocked by config")
            }
        }
    }

    private suspend fun getPrefs(channel: Channel) = Datastore.GuildPrefsCollection.get(channel.data.guildId.value?.value.toString())
}