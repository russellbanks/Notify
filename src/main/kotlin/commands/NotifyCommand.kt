package commands

import dev.kord.core.entity.Member
import dev.kord.core.entity.Message
import io.klogging.Klogging
import kord
import kotlinx.coroutines.delay

object NotifyCommand: Klogging {

    /**
     * Public function that checks whether the member ran the command inside or outside a vc
     *
     * @param member [Member] - The member that ran the command
     * @param message [Message] - The command message
     */
    suspend fun run(member: Member, message: Message) {
        logger.info("${member.tag} ran ${Command.Notify.actionName}")
        if (member.getVoiceStateOrNull()?.channelId != null) sendReply(member, message)
        else sendInvalidReply(member, message)
    }

    /**
     * Sends the message that tags everyone and states what members are in a voice channel
     *
     * @param member [Member] - The member that ran the command
     * @param message [Message] - The command message
     */
    private suspend fun sendReply(member: Member, message: Message) {
        message.channel.createMessage(getReply(member))
        runCatching { message.delete() }.onFailure { logger.trace("Unable to delete '${message.id}' from ${member.tag} due to $it") }
    }

    /**
     * Sends a message telling the member that they must be in a voice channel to use the command
     *
     * @param member [Member] - The member that ran the command
     * @param message [Message] - The command message
     */
    private suspend fun sendInvalidReply(member: Member, message: Message) {
        val botMessage = message.channel.createMessage("${member.mention}, you must be in a voice channel to use this command.")
        runCatching { message.delete() }.onFailure { logger.trace("Unable to delete '${message.id}' from ${member.tag} due to $it") }
        runCatching {
            delay(10000)
            botMessage.delete()
        }.onFailure { logger.trace("Unable to delete '${botMessage.id}' from ${botMessage.author?.tag} due to $it") }
    }

    /**
     * Builds the message that tags everyone and states what members are in a voice channel
     *
     * @param member [Member] - The member that ran the command
     */
    private suspend fun getReply(member: Member) = "@everyone, ${member.mention} is in **${getChannelName(member)}** ${getFormattedListOfMembers(getListOfVCMembers(member))}"

    /**
     * Gets a mutable list of a valid members inside the voice channel that the member is in
     *
     * @param member [Member] - The member that ran the command
     */
    private suspend fun getListOfVCMembers(member: Member) = mutableListOf<Member>().also { list ->
        member.getVoiceState().getChannelOrNull()?.voiceStates?.collect {
            if (it.getMemberOrNull() != null && it.getMember() != member && !it.getMember().isBot) list.add(it.getMember())
        }
    }

    /**
     * Formats a mutable list of voice members inside a voice channel into a readable string
     *
     * @param listOfVoiceMembers [MutableList] - A list of valid members inside a voice channel
     */
    private fun getFormattedListOfMembers(listOfVoiceMembers: MutableList<Member>) = listOfVoiceMembers.run {
        when (size) {
            0 -> ""
            1 -> "with ${first().mention}"
            else -> {
                val lastMember = last()
                remove(last())
                "with ${joinToString { it.mention }} and ${lastMember.mention}"
            }
        }
    }

    /**
     * Gets the name of the voice channel that the member is currently in
     *
     * @param member [Member] - The member that ran the command
     */
    private suspend fun getChannelName(member: Member) = kord.getChannel(member.getVoiceState().channelId!!)?.data?.name?.value
}