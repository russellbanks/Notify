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

import dev.kord.core.behavior.MemberBehavior
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.count
import kotlinx.coroutines.flow.filterNot
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.toList

object NotifyReply {

    /**
     * Public function to retrieve the response that Notifies the chosen target
     *
     * @param member [MemberBehavior] -  The member that ran the command
     * @param target [NotifyTarget] - The scope of whom the notifications should be sent to
     */
    suspend fun getNotifyReply(member: MemberBehavior, target: NotifyTarget): String {
        return if (member.getVoiceStateOrNull()?.channelId != null) {
            getValidReply(member, target)
        } else {
            getInvalidReply(member)
        }
    }

    /**
     * Builds the message that tags everyone and states what members are in a voice channel
     *
     * @param member [MemberBehavior] - The member that ran the command
     */
    suspend fun getValidReply(member: MemberBehavior, target: NotifyTarget) = "@${target.readableName}, ${member.mention} is in ${member.getVoiceState().getChannelOrNull()?.mention} ${
        getFormattedStringOfMembers(getListOfVoiceMembers(member))
    }"

    /**
     * Builds the message that tells the user that they need to be in a voice channel to use the command
     *
     * @param member [MemberBehavior] - The member that ran the command
     */
    fun getInvalidReply(member: MemberBehavior) = "${member.mention}, you must be in a voice channel to use this command."

    /**
     * Gets a flow of all valid members inside the voice channel that the member is in
     *
     * @param member [MemberBehavior] - The member that ran the command
     * @return [Flow] - A flow containing all valid members in a voice channel
     */
    private suspend fun getListOfVoiceMembers(member: MemberBehavior): Flow<MemberBehavior> {
        return flow {
            member.getVoiceState().getChannelOrNull()?.voiceStates?.filterNot {
                it.getMember() == member || it.getMember().isBot
            }?.collect {
                emit(it.getMember())
            }
        }
    }

    /**
     * Formats a flow of valid voice members inside a voice channel into a readable string.
     *
     * @param membersFlow [Flow] - A flow of valid members inside a voice channel
     * @return [String] - A readable ending string of voice members
     */
    private suspend fun getFormattedStringOfMembers(membersFlow: Flow<MemberBehavior>): String {
        return when (membersFlow.count()) {
            0 -> ""
            1 -> "with ${membersFlow.first().mention}"
            else -> {
                val finalMember = membersFlow.last()
                val finalMembersFlow = membersFlow.filterNot { it == membersFlow.last() }
                "with ${finalMembersFlow.map { it.mention }.toList().joinToString()} and ${finalMember.mention}"
            }
        }
    }

}