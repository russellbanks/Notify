package interactions

import bot
import dev.kord.core.behavior.interaction.followUp
import dev.kord.core.behavior.interaction.followUpEphemeral
import dev.kord.core.entity.Member
import dev.kord.core.entity.interaction.ChatInputCommandInvocationInteraction

class NotifyInteraction(override val name: String = "notify"): InteractionCommand() {

    override suspend fun main(interaction: ChatInputCommandInvocationInteraction) {
        val member = interaction.user.asMember(interaction.data.guildId.value!!)

        if (member.getVoiceState().channelId == null) {
            interaction.acknowledgeEphemeral().followUpEphemeral {
                content = "You must be in a voice channel to run this."
            }
            throw Error("A member ran Notify and was not in a voice channel")
        }

        interaction.acknowledgePublic().followUp {
            content = "@everyone, ${member.nicknameMention} is in **${getChannelName(member)}** ${getFormattedListOfMembers(getListOfVCMembers(member))}"
        }
    }

    /**
     * Gets a mutable list of a valid members inside the voice channel that the member is in
     *
     * @param member [Member] - The member that ran the command
     */
    private suspend fun getListOfVCMembers(member: Member) = mutableListOf<String>().also { otherMemberMentions ->
        member.getVoiceState().getChannelOrNull()?.voiceStates?.collect { voiceState ->
            voiceState.getMemberOrNull()?.run {
                if (this != member && !isBot) otherMemberMentions.add(nicknameMention)
            }
        }
    }

    /**
     * Formats a mutable list of voice members inside a voice channel into a readable string
     *
     * @param listOfVoiceMembers [MutableList] - A list of valid members inside a voice channel
     */
    private fun getFormattedListOfMembers(listOfVoiceMembers: MutableList<String>) = listOfVoiceMembers.run {
        when (size) {
            0 -> ""
            1 -> "with ${first()}"
            else -> {
                val finalMember = last()
                removeLast()
                "with ${joinToString()} and $finalMember"
            }
        }
    }

    /**
     * Gets the name of the voice channel that the member is currently in
     *
     * @param member [Member] - The member that ran the command
     */
    private suspend fun getChannelName(member: Member) = bot.client.getChannel(member.getVoiceState().channelId!!)?.data?.name?.value

}