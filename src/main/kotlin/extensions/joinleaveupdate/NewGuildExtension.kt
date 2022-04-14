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

package extensions.joinleaveupdate

import com.kotlindiscord.kord.extensions.extensions.Extension
import com.kotlindiscord.kord.extensions.extensions.event
import data.Database
import dev.kord.common.entity.ChannelType
import dev.kord.core.behavior.channel.MessageChannelBehavior
import dev.kord.core.event.guild.GuildCreateEvent
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.first

class NewGuildExtension: Extension() {

    override val name = "new-guild"
    override suspend fun setup() {
        event<GuildCreateEvent> {
            action {
                if (Database.isNewGuild(event.guild)) {
                    Database.createGuild(event.guild)
                    val topTextChannel = event.guild.channels.filter { it.type == ChannelType.GuildText }.first()
                    Database.updateChannel(event.guild, topTextChannel)
                    MessageChannelBehavior(topTextChannel.id, kord).createMessage("Thanks for inviting me! For now, I have set ${topTextChannel.mention} as the text channel for voice state notifications. This can be configured with `/configure channel`.")
                }
            }
        }
    }
}