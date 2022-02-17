/**

Notify
Copyright (C) 2022  BanDev

This program is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program.  If not, see <https://www.gnu.org/licenses/>.

 */

import dev.kord.core.entity.Member
import dev.kord.core.entity.Message
import io.klogging.Klogging
import kotlinx.coroutines.delay

object FallbackNotifyCommand: Klogging {

    /**
     * Public function that checks whether the member ran the command inside or outside a vc
     *
     * @param member [Member] - The member that ran the command
     * @param message [Message] - The command message
     */
    suspend fun sendReply(member: Member, message: Message) {
        if (member.getVoiceStateOrNull()?.channelId != null) sendValidReply(member, message)
        else sendInvalidReply(member, message)
    }

    /**
     * Sends the message that tags everyone and states what members are in a voice channel
     *
     * @param member [Member] - The member that ran the command
     * @param message [Message] - The command message
     */
    private suspend fun sendValidReply(member: Member, message: Message) {
        message.channel.createMessage(NotifyReply.getValidReply(member))
        runCatching { message.delete() }.onFailure { logger.trace("Unable to delete '${message.id}' from ${member.tag} due to $it") }
    }

    /**
     * Sends a message telling the member that they must be in a voice channel to use the command
     *
     * @param member [Member] - The member that ran the command
     * @param message [Message] - The command message
     */
    private suspend fun sendInvalidReply(member: Member, message: Message) {
        val botMessage = message.channel.createMessage(NotifyReply.getInvalidReply(member))
        runCatching { message.delete() }.onFailure { logger.trace("Unable to delete '${message.id}' from ${member.tag} due to $it") }
        runCatching {
            delay(10000)
            botMessage.delete()
        }.onFailure { logger.trace("Unable to delete '${botMessage.id}' from ${botMessage.author?.tag} due to $it") }
    }

}