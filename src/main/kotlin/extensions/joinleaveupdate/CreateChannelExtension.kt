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

package extensions.joinleaveupdate

import com.kotlindiscord.kord.extensions.extensions.Extension
import com.kotlindiscord.kord.extensions.extensions.event
import data.Database
import dev.kord.common.entity.ChannelType
import dev.kord.core.event.channel.TextChannelCreateEvent
import kotlinx.coroutines.flow.count

class CreateChannelExtension: Extension() {

    override val name = "channel-create-event"

    override suspend fun setup() {
        event<TextChannelCreateEvent> {
            action {
                event.channel.guild.also { guild ->
                    if (guild.channels.count { it.type == ChannelType.GuildText } == 1) {
                        Database.updateChannel(guild, event.channel)
                    }
                }
            }
        }
    }
}