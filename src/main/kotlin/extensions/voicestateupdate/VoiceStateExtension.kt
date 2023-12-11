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

package extensions.voicestateupdate

import EnvironmentVariables
import com.kotlindiscord.kord.extensions.extensions.Extension
import com.kotlindiscord.kord.extensions.extensions.event
import com.russellbanks.data.GuildPrefs
import data.Dao
import dev.kord.common.Color
import dev.kord.common.entity.Snowflake
import dev.kord.core.behavior.channel.MessageChannelBehavior
import dev.kord.core.behavior.channel.createEmbed
import dev.kord.core.entity.Member
import dev.kord.core.event.user.VoiceStateUpdateEvent
import kotlinx.datetime.Clock

class VoiceStateExtension: Extension() {
    override val name = "voice-state"

    override suspend fun setup() {
        event<VoiceStateUpdateEvent> {
            action {
                val action = getAction(event)

                val channel = if (action == Action.LEAVE) {
                    event.old?.getChannelOrNull()
                } else {
                    event.state.getChannelOrNull()
                }
                val guildPrefs = Dao.get(event.state.getGuild())

                val member = event.state.getMember()
                if (shouldSendEmbed(action, guildPrefs, member)) {
                    guildPrefs.channelId?.let(::Snowflake)?.let { snowflake ->
                        MessageChannelBehavior(snowflake, kord).createEmbed {
                            color = Color(EnvironmentVariables.accentColor()[0], EnvironmentVariables.accentColor()[1], EnvironmentVariables.accentColor()[2])
                            title = "${member.username} ${action?.phrase} ${channel?.asChannel()?.name}"
                            timestamp = Clock.System.now()
                            author {
                                name = member.username
                                icon = member.avatar?.cdnUrl?.toUrl()
                            }
                            footer {
                                action?.emoji?.unicode?.let {
                                    text = it
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private suspend fun getAction(event: VoiceStateUpdateEvent): Action? = when {
        event.old?.getChannelOrNull() == null -> Action.JOIN
        event.old?.getChannelOrNull() != event.state.getChannelOrNull()
            && event.state.getChannelOrNull() != null -> Action.SWITCH
        event.state.getChannelOrNull() == null -> Action.LEAVE
        event.old?.isSelfStreaming == false && event.state.isSelfStreaming -> Action.STREAM
        else -> null
    }

    private fun shouldSendEmbed(action: Action?, guildPrefs: GuildPrefs, member: Member): Boolean {
        return when {
            member.isBot -> false
            action == Action.JOIN && !guildPrefs.joinPref -> false
            action == Action.LEAVE && !guildPrefs.leave -> false
            action == Action.SWITCH && !guildPrefs.switch -> false
            action == Action.STREAM && !guildPrefs.stream -> false
            action == Action.VIDEO && !guildPrefs.video -> false
            action == null -> false
            else -> true
        }
    }
}
