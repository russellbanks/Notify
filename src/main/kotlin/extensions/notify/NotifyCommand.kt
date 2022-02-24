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

import com.kotlindiscord.kord.extensions.checks.isNotBot
import com.kotlindiscord.kord.extensions.extensions.Extension
import dev.kord.core.entity.Member
import io.github.qbosst.kordex.commands.hybrid.publicHybridCommand
import io.github.qbosst.kordex.commands.hybrid.publicSubCommand

class NotifyCommand: Extension() {
    override val name = "Notify"

    override suspend fun setup() {
        publicHybridCommand {
            name = "Notify"
            description = "Notify"

            check { isNotBot() }

            publicSubCommand {
                name = "everyone"
                description = "Notify everyone that you are in a voice channel"
                action {
                    respond {
                        content = if (member?.getVoiceStateOrNull()?.channelId != null) {
                            NotifyReply.getValidReply(member as Member, NotifyTarget.EVERYONE)
                        } else {
                            NotifyReply.getInvalidReply(member as Member)
                        }
                    }
                }
            }

            publicSubCommand {
                name = "here"
                description = "Notify people who are online that you are in a voice channel"
                action {
                    respond {
                        content = if (member?.getVoiceStateOrNull()?.channelId != null) {
                            NotifyReply.getValidReply(member as Member, NotifyTarget.HERE)
                        } else {
                            NotifyReply.getInvalidReply(member as Member)
                        }
                    }
                }
            }
        }
    }
}