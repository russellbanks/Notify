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

package extensions.notify

import dev.kord.core.entity.Member

internal object NotifyReply {

    /**
     * Public function to retrieve the response that Notifies the chosen target
     *
     * @param member [Member] -  The member that ran the command
     * @param target [NotifyTarget] - The scope of whom the notifications should be sent to
     */
    suspend fun getNotifyReply(member: Member, target: NotifyTarget): String {
        return if (member.getVoiceStateOrNull()?.channelId != null) {
            getValidReply(member, target)
        } else {
            getInvalidReply(member)
        }
    }

    /**
     * Builds the message that tags everyone and states what members are in a voice channel
     *
     * @param member [Member] - The member that ran the command
     */
    private suspend fun getValidReply(member: Member, target: NotifyTarget) = "${getTarget(target)}, ${member.mention} is in **${member.getVoiceState().getChannelOrNull()?.mention}** ${
        getFormattedListOfMembers(getListOfVCMembers(member))
    }"

    /**
     * Builds the message that tells the user that they need to be in a voice channel to use the command
     *
     * @param member [Member] - The member that ran the command
     */
    private fun getInvalidReply(member: Member) = "${member.mention}, you must be in a voice channel to use this command."

    /**
     * Returns @here or @everyone based on the NotifyTarget
     *
     * @param target [NotifyTarget]
     */
    private fun getTarget(target: NotifyTarget) = if (target == NotifyTarget.HERE) "@here" else "@everyone"

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

}