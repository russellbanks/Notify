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