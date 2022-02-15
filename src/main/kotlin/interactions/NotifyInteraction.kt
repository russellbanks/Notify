package interactions

import NotifyReply
import dev.kord.core.behavior.interaction.followUp
import dev.kord.core.behavior.interaction.followUpEphemeral
import dev.kord.core.entity.interaction.ChatInputCommandInvocationInteraction

class NotifyInteraction(override val name: String = "notify"): InteractionCommand() {

    override suspend fun main(interaction: ChatInputCommandInvocationInteraction) {
        val member = interaction.user.asMember(interaction.data.guildId.value!!)

        if (member.getVoiceStateOrNull()?.channelId == null) {
            interaction.acknowledgeEphemeral().followUpEphemeral {
                content = NotifyReply.getInvalidReply(member)
            }
        } else {
            interaction.acknowledgePublic().followUp {
                content = NotifyReply.getValidReply(member)
            }
        }
    }

}