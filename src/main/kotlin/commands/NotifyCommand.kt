package commands

import dev.kord.core.entity.Member
import dev.kord.core.entity.Message
import io.klogging.Klogging
import kord
import kotlinx.coroutines.delay

object NotifyCommand: Klogging {

    suspend fun run(member: Member, message: Message) {
        logger.info("${member.tag} ran ${Command.Notify.actionName}")
        if (member.getVoiceStateOrNull()?.channelId != null) {
            message.channel.createMessage(getReply(member))
            runCatching { message.delete() }.onFailure { logger.trace("Unable to delete '${message.id}' from ${member.tag} due to $it") }
        } else {
            val botMessage = message.channel.createMessage("${member.mention}, you must be in a voice channel to use this command.")
            runCatching { message.delete() }.onFailure { logger.trace("Unable to delete '${message.id}' from ${member.tag} due to $it") }
            runCatching {
                delay(10000)
                botMessage.delete()
            }.onFailure { logger.trace("Unable to delete '${botMessage.id}' due to $it") }
        }
    }

    private suspend fun getReply(member: Member) = "@everyone, ${member.mention} is in **${getChannelName(member)}** ${getFormattedListOfMembers(getListOfVCMembers(member))}"

    private suspend fun getListOfVCMembers(member: Member) = mutableListOf<Member>().also { list ->
        member.getVoiceState().getChannelOrNull()?.voiceStates?.collect {
            if (it.getMemberOrNull() != null && it.getMember() != member && !it.getMember().isBot) list.add(it.getMember())
        }
    }

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

    private suspend fun getChannelName(member: Member) = kord.getChannel(member.getVoiceState().channelId!!)?.data?.name?.value
}