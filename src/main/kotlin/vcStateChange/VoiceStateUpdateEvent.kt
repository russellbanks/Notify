package vcStateChange

import dev.kord.common.Color
import dev.kord.common.entity.Snowflake
import dev.kord.core.behavior.channel.MessageChannelBehavior
import dev.kord.core.behavior.channel.createEmbed
import dev.kord.core.entity.Member
import dev.kord.core.entity.channel.Channel
import dev.kord.core.event.user.VoiceStateUpdateEvent
import dev.kord.core.on
import io.klogging.Klogging
import kord
import kotlinx.datetime.Clock
import db.Connection.getGuildPrefs

/**
 * Triggers when a voice call's state changes
 */

object VoiceStateUpdateEvent: Klogging {
    fun listener() {
        kord.on<VoiceStateUpdateEvent> {
            // Decide on the action
            val action = when {
                (old?.getChannelOrNull() == null) -> Action.JOIN
                (old?.getChannelOrNull() != state.getChannelOrNull() && state.getChannelOrNull() != null) -> Action.SWITCH
                (state.getChannelOrNull() == null) -> Action.LEAVE
                (old?.isSelfSteaming == false && state.isSelfSteaming) -> Action.STREAM
                else -> Action.UNKNOWN
            }

            // Get member and channel details,
            // or give up if they can't be found
            val member = state.getMemberOrNull() ?: return@on
            val channel = if (action == Action.LEAVE) kord.getChannel(old?.channelId!!) ?: return@on
            else kord.getChannel(state.channelId!!) ?: return@on

            if (member.isBot) return@on

            // Send the embed
            sendEmbed(member, channel, action)
        }
    }

    /**
     * Send an embed to "vcupdates" in
     * response to a voice channel update.
     *
     * @param member [Member] - The member who made the change
     * @param channel [Channel] - The voice channel
     * @param action [Action] - The action carried out
     */

    private suspend fun sendEmbed(member: Member, channel: Channel, action: Action) {
        // If the action is unknown, don't send it
        if (action == Action.UNKNOWN) return
        // Find the guild preferences from db
        val prefs = getGuildPrefs(channel.data.guildId.value?.value.toString())
        // Don't show if the feature is disabled
        for(feature in prefs.getDisabled()) if(feature.uppercase() == action.name) {
            logger.info("[ ${member.tag} | ${action.name} | ${channel.data.name.value} | Blocked by guild config ]")
            return
        }
        // Build the embed
        val embed = MessageChannelBehavior(Snowflake(prefs.channelId), kord).createEmbed {
            color = Color(0x00, 0x67, 0xf4)
            title = "${member.displayName} ${action.text} ${channel.data.name.value}"
            timestamp = Clock.System.now()
            author {
                name = member.displayName
                icon = member.avatar?.url
            }
            footer {
                text = action.emoji
            }
        }
        logger.info("[ ${member.tag} | ${action.name} | ${channel.data.name.value} | Embed id: ${embed.data.id.value} ]")
    }
}
