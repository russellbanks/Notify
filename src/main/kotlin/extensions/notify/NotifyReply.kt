/**

Notify
Copyright (C) 2023 Russell Banks

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
import dev.kord.core.entity.Member
import dev.kord.core.entity.VoiceState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterNot
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.toList

object NotifyReply {
    suspend fun getValidReply(member: MemberBehavior, target: NotifyTarget): String {
        val memberMention = member.mention
        val channelMention = member.getVoiceState().getChannelOrNull()?.mention
        val voiceMembers = getListOfVoiceMembers(member)
        val memberString = getFormattedStringOfMembers(voiceMembers)

        return "@${target.readableName}, $memberMention is in $channelMention $memberString"
    }

    fun getInvalidReply(member: MemberBehavior): String {
        return "${member.mention}, you must be in a voice channel to use this command."
    }

    private suspend fun getListOfVoiceMembers(member: MemberBehavior): Flow<Member>? = member
        .getVoiceState()
        .getChannelOrNull()
        ?.voiceStates
        ?.filterNot { it.getMember() == member || it.getMember().isBot }
        ?.map(VoiceState::getMember)

    private suspend fun getFormattedStringOfMembers(members: Flow<Member>?): String {
        val membersList = members?.toList() ?: emptyList()

        return when (membersList.size) {
            0 -> ""
            1 -> "with ${membersList.first().mention}"
            else -> membersList
                .dropLast(1)
                .joinToString(
                    separator = ", ",
                    prefix = "with ",
                    postfix = "and ${membersList.last().mention}",
                    transform = Member::mention
                )
        }
    }
}
