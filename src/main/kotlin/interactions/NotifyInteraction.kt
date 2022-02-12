package interactions

import bot
import dev.kord.core.behavior.interaction.followUp
import dev.kord.core.behavior.interaction.followUpEphemeral
import dev.kord.core.entity.interaction.ChatInputCommandInvocationInteraction

class NotifyInteraction(override val name: String = "notify"): InteractionCommand() {

    override suspend fun main(interaction: ChatInputCommandInvocationInteraction) {
        // Get calling member details
        val member = interaction.user.asMember(interaction.data.guildId.value!!)

        // See if the calling member is in a vc on the guild
        if(member.getVoiceStateOrNull() === null) {
            // User is not in a guild vc
            interaction.acknowledgeEphemeral().followUpEphemeral {
                content = "You must be in a vc to run this"
            }
            throw Error("User not in a guild vc")
        }

        // Get the vc the calling member is in
        val vc = bot.client.getChannel(member.getVoiceState().channelId!!)?.data

        // Create an empty list of other members, no need to
        // store their whole object, only their mention
        val otherMemberMentions = mutableListOf<String>()

        // Collect the voice state of the other members
        member.getVoiceState().getChannelOrNull()?.voiceStates?.collect { voiceState ->
            // If other member for whatever reason is null
            if(voiceState.getMemberOrNull() == null) return@collect
            // Get other member knowing they are not null
            val otherMember = voiceState.getMember()
            // If other member is the calling member
            if(otherMember == member) return@collect
            // If other member is a bot
            if(otherMember.isBot) return@collect
            // Assuming they are fine, add to the list
            otherMemberMentions.add(voiceState.getMember().nicknameMention)
        }

        // If there is only one member in the vc
        val after = when (otherMemberMentions.size) {
            0 -> ""
            1 -> "with ${otherMemberMentions[0]}"
            else -> {
                val finalMember = otherMemberMentions.last()
                otherMemberMentions.removeLast()
                "with ${otherMemberMentions.joinToString()} and $finalMember"
            }
        }

        // Acknowledge the interaction publicly
        interaction.acknowledgePublic().followUp {
            content = "@everyone, ${member.nicknameMention} is in **${vc?.name?.value}** $after"
        }
    }
}