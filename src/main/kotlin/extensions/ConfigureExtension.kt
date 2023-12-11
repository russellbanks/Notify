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

package extensions

import EnvironmentVariables
import com.kotlindiscord.kord.extensions.checks.hasPermission
import com.kotlindiscord.kord.extensions.checks.isNotBot
import com.kotlindiscord.kord.extensions.commands.Arguments
import com.kotlindiscord.kord.extensions.commands.application.slash.ephemeralSubCommand
import com.kotlindiscord.kord.extensions.commands.converters.impl.channel
import com.kotlindiscord.kord.extensions.components.ComponentContainer
import com.kotlindiscord.kord.extensions.components.components
import com.kotlindiscord.kord.extensions.components.publicButton
import com.kotlindiscord.kord.extensions.components.types.emoji
import com.kotlindiscord.kord.extensions.extensions.Extension
import com.kotlindiscord.kord.extensions.extensions.ephemeralSlashCommand
import data.Dao
import dev.kord.common.Color
import dev.kord.common.entity.ButtonStyle
import dev.kord.common.entity.ChannelType
import dev.kord.common.entity.Permission
import dev.kord.core.behavior.GuildBehavior
import dev.kord.rest.builder.message.EmbedBuilder
import dev.kord.rest.builder.message.embed
import dev.kord.x.emoji.Emojis
import extensions.voicestateupdate.Action

class ConfigureExtension: Extension() {
    override val name = "configure"

    override suspend fun setup() {
        ephemeralSlashCommand {
            name = "configure"
            description = "Configure the server's current preferences"

            check { isNotBot() }

            ephemeralSubCommand {
                name = "notifications"
                description = "Configure which events should trigger a notification"

                check { hasPermission(Permission.ManageGuild) }

                action {
                    guild?.let { guild ->
                        respond {
                            embed(fun EmbedBuilder.() {
                                author {
                                    name = "${guild.asGuild().name} configuration"
                                    icon = guild.asGuild().icon?.cdnUrl?.toUrl()
                                }
                                color = Color(EnvironmentVariables.accentColor()[0], EnvironmentVariables.accentColor()[1], EnvironmentVariables.accentColor()[2])
                                for (action in Action.entries) {
                                    field(
                                        buildString {
                                            append(action.name.lowercase().replaceFirstChar(Char::titlecase))
                                            append(' ')
                                            if (getActionToggle(action, guild)) {
                                                append(Emojis.whiteCheckMark.unicode)
                                            } else {
                                                append(Emojis.x.unicode)
                                            }
                                        }
                                    )
                                }
})
                            components {
                                if (member != null) {
                                    for (action in Action.entries) {
                                        publicActionButton(action, guild)
                                    }
                                }
                            }
                        }
                    }
                }
            }

            ephemeralSubCommand(::ChannelArgs) {
                name = "channel"
                description = "Set which channel notifications should be sent in"

                check { hasPermission(Permission.ManageGuild) }

                action {
                    respond {
                        guild?.let {
                            content = if (Dao.get(it).channelId != arguments.scope.id.toString()) {
                                Dao.updateChannel(it, arguments.scope)
                                "${
                                    if (Dao.get(it).channelId == arguments.scope.id.toString()) "Successfully" 
                                    else "Failed to"
                                } set ${arguments.scope.mention} as the text channel to send voice state notifications in."
                            } else {
                                "${arguments.scope.mention} is already set as the text channel to send voice state notifications in."
                            }
                        }
                    }
                }
            }
        }
    }

    inner class ChannelArgs : Arguments() {
        val scope by channel {
            name = "channel"
            description = "channel description"
            requiredChannelTypes = mutableSetOf(ChannelType.GuildText)
        }
    }

    private suspend fun ComponentContainer.publicActionButton(action: Action, guild: GuildBehavior) {
        publicButton {
            label = action.name.lowercase().replaceFirstChar(Char::titlecase)
            style = ButtonStyle.Secondary
            emoji(action.emoji.unicode)
            deferredAck = true
            action {
                Dao.update(guild, action, !getActionToggle(action, guild))
                edit {
                    embed {
                        author {
                            name = "${guild.asGuild().name} configuration"
                            icon = guild.asGuild().icon?.cdnUrl?.toUrl()
                        }
                        color = Color(EnvironmentVariables.accentColor()[0], EnvironmentVariables.accentColor()[1], EnvironmentVariables.accentColor()[2])
                        for (actionItem in Action.entries) {
                            field(
                                buildString {
                                    append(actionItem.name.lowercase().replaceFirstChar(Char::titlecase))
                                    append(' ')
                                    if (getActionToggle(actionItem, guild)) {
                                        append(Emojis.whiteCheckMark.unicode)
                                    } else {
                                        append(Emojis.x.unicode)
                                    }
                                }
                            )
                        }
                    }
                }
            }
        }
    }

    private suspend fun getActionToggle(action: Action, guild: GuildBehavior): Boolean {
        val guildPrefs = Dao.get(guild)
        return when (action) {
            Action.JOIN -> guildPrefs.joinPref
            Action.LEAVE -> guildPrefs.leave
            Action.SWITCH -> guildPrefs.switch
            Action.STREAM -> guildPrefs.stream
            Action.VIDEO -> guildPrefs.video
        }
    }
}
