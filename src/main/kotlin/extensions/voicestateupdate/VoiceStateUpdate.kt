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

package extensions.voicestateupdate

import com.kotlindiscord.kord.extensions.extensions.Extension
import com.kotlindiscord.kord.extensions.extensions.event
import data.Datastore
import dev.kord.common.Color
import dev.kord.common.entity.Snowflake
import dev.kord.core.behavior.channel.MessageChannelBehavior
import dev.kord.core.behavior.channel.createEmbed
import dev.kord.core.event.user.VoiceStateUpdateEvent
import kotlinx.datetime.Clock

class VoiceStateUpdate: Extension() {
    override val name = "Voice State Update"

    override suspend fun setup() {
        event<VoiceStateUpdateEvent> {
            action {
                val action = when {
                    (event.old?.getChannelOrNull() == null) -> Action.JOIN
                    (event.old?.getChannelOrNull() != event.state.getChannelOrNull() && event.state.getChannelOrNull() != null) -> Action.SWITCH
                    (event.state.getChannelOrNull() == null) -> Action.LEAVE
                    (event.old?.isSelfSteaming == false && event.state.isSelfSteaming) -> Action.STREAM
                    else -> Action.VIDEO
                }

                val channelId = if (action == Action.LEAVE) event.old?.channelId!! else event.state.channelId!!
                val channel = event.kord.getChannel(channelId)
                val prefs = Datastore.GuildPrefsCollection.get(event.state.getMember().guildId)

                val member = event.state.getMember()
                MessageChannelBehavior(Snowflake(prefs.channelId), kord).createEmbed {
                    color = Color(Config.accentColor()[0], Config.accentColor()[1], Config.accentColor()[2])
                    title = "${member.displayName} ${action.text} ${channel?.data?.name?.value}"
                    timestamp = Clock.System.now()
                    author {
                        name = member.displayName
                        icon = member.avatar?.url
                    }
                    footer {
                        text = action.emoji.unicode
                    }
                }
            }
        }
    }
}