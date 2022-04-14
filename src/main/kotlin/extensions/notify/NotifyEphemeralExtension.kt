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

import com.kotlindiscord.kord.extensions.checks.hasPermission
import com.kotlindiscord.kord.extensions.checks.isNotBot
import com.kotlindiscord.kord.extensions.commands.Arguments
import com.kotlindiscord.kord.extensions.commands.application.slash.converters.impl.enumChoice
import com.kotlindiscord.kord.extensions.extensions.Extension
import com.kotlindiscord.kord.extensions.modules.unsafe.annotations.UnsafeAPI
import com.kotlindiscord.kord.extensions.modules.unsafe.extensions.unsafeSlashCommand
import com.kotlindiscord.kord.extensions.modules.unsafe.types.InitialSlashCommandResponse
import com.kotlindiscord.kord.extensions.modules.unsafe.types.ackEphemeral
import com.kotlindiscord.kord.extensions.modules.unsafe.types.ackPublic
import com.kotlindiscord.kord.extensions.modules.unsafe.types.respondEphemeral
import com.kotlindiscord.kord.extensions.modules.unsafe.types.respondPublic
import com.kotlindiscord.kord.extensions.utils.scheduling.Scheduler
import dev.kord.common.entity.Permission
import kotlin.time.Duration.Companion.hours

class NotifyEphemeralExtension: Extension() {
    override val name = "notify-ephemeral"

    @OptIn(UnsafeAPI::class)
    override suspend fun setup() {
        unsafeSlashCommand(::Args) {
            name = "Notify"
            description = "Notify"
            initialResponse = InitialSlashCommandResponse.None

            check { isNotBot() }
            check { hasPermission(Permission.MentionEveryone) }

            action {
                member?.let {
                    if (it.getVoiceStateOrNull()?.getChannelOrNull() != null) {
                        ackPublic()
                        respondPublic {
                            content = NotifyReply.getValidReply(it, arguments.scope)
                        }
                    } else {
                        ackEphemeral()
                        respondEphemeral {
                            content = NotifyReply.getInvalidReply(it)
                        }
                    }
                }
            }
        }
    }

    inner class Args : Arguments() {
        val scope by enumChoice<NotifyTarget> {
            typeName = "notify target"
            name = "target"
            description = "The target"
        }
    }

}