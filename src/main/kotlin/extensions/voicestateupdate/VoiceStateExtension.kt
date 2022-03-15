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

import EnvironmentVariables
import com.kotlindiscord.kord.extensions.extensions.Extension
import com.kotlindiscord.kord.extensions.extensions.event
import data.Datastore
import data.GuildPrefs
import dev.kord.common.Color
import dev.kord.common.entity.Snowflake
import dev.kord.core.behavior.channel.MessageChannelBehavior
import dev.kord.core.behavior.channel.createEmbed
import dev.kord.core.event.user.VoiceStateUpdateEvent
import kotlinx.datetime.Clock

class VoiceStateExtension: Extension() {
    override val name = "voice-state"

    override suspend fun setup() {
        event<VoiceStateUpdateEvent> {
            action {
                val action = when {
                    (event.old?.getChannelOrNull() == null) -> Action.JOIN
                    (event.old?.getChannelOrNull() != event.state.getChannelOrNull() && event.state.getChannelOrNull() != null) -> Action.SWITCH
                    (event.state.getChannelOrNull() == null) -> Action.LEAVE
                    (event.old?.isSelfSteaming == false && event.state.isSelfSteaming) -> Action.STREAM
                    else -> Action.UNKNOWN
                }

                val channelId = if (action == Action.LEAVE) event.old?.channelId!! else event.state.channelId!!
                val channel = kord.getChannel(channelId)!!
                val prefs = Datastore.GuildPrefsCollection.get(event.state.getGuild())

                val member = event.state.getMember()
                prefs.channelId?.let {
                    if (shouldSendEmbed(action, prefs)) {
                        MessageChannelBehavior(Snowflake(it), kord).createEmbed {
                            color = Color(EnvironmentVariables.accentColor()[0], EnvironmentVariables.accentColor()[1], EnvironmentVariables.accentColor()[2])
                            title = "${member.displayName} ${action.phrase} ${channel.data.name.value}"
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
    }

    private fun shouldSendEmbed(action: Action, prefs: GuildPrefs): Boolean {
        return when {
            action == Action.JOIN && !prefs.join -> false
            action == Action.LEAVE && !prefs.leave -> false
            action == Action.SWITCH && !prefs.switch -> false
            action == Action.STREAM && !prefs.stream -> false
            action == Action.VIDEO && !prefs.video -> false
            action == Action.UNKNOWN -> false
            else -> true
        }
    }
}