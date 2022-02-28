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
import com.kotlindiscord.kord.extensions.extensions.publicSlashCommand
import com.kotlindiscord.kord.extensions.types.respond
import dev.kord.common.entity.Permission
import dev.kord.core.entity.Member

class NotifyEphemeralCommand: Extension() {
    override val name = "NotifyEphemeral"

    override suspend fun setup() {
        publicSlashCommand(::Args) {
            name = "Notify"
            description = "Notify"

            check { isNotBot() }
            check { hasPermission(Permission.MentionEveryone) }

            action {
                respond {
                    content = NotifyReply.getNotifyReply(member as Member, arguments.scope)
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