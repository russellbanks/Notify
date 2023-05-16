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
import data.Dao
import dev.kord.common.entity.ChannelType
import dev.kord.core.event.channel.TextChannelDeleteEvent
import kotlinx.coroutines.flow.count
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.first

class DeleteChannelExtension: Extension() {

    override val name = "channel-delete-event"

    override suspend fun setup() {
        event<TextChannelDeleteEvent> {
            action {
                val guild = event.channel.guild
                if (guild.channels.count { it.type == ChannelType.GuildText } == 0) {
                    Dao.updateChannel(guild, null)
                } else if (Dao.get(guild).channelId == event.channel.id.toString()) {
                    Dao.updateChannel(guild, guild.channels.filter { it.type == ChannelType.GuildText }.first())
                }
            }
        }
    }
}
