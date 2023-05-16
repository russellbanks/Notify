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
import com.kotlindiscord.kord.extensions.commands.chat.ChatCommandRegistry
import com.kotlindiscord.kord.extensions.commands.chat.ChatGroupCommand
import com.kotlindiscord.kord.extensions.commands.converters.impl.channel
import com.kotlindiscord.kord.extensions.components.ComponentContainer
import com.kotlindiscord.kord.extensions.components.components
import com.kotlindiscord.kord.extensions.components.publicButton
import com.kotlindiscord.kord.extensions.components.types.emoji
import com.kotlindiscord.kord.extensions.extensions.Extension
import com.kotlindiscord.kord.extensions.extensions.ephemeralSlashCommand
import com.kotlindiscord.kord.extensions.types.edit
import com.kotlindiscord.kord.extensions.types.respond
import data.Database
import dev.kord.common.Color
import dev.kord.common.entity.ButtonStyle
import dev.kord.common.entity.ChannelType
import dev.kord.common.entity.Permission
import dev.kord.common.entity.Snowflake
import dev.kord.core.behavior.GuildBehavior
import dev.kord.core.behavior.channel.createMessage
import dev.kord.rest.Image
import dev.kord.rest.builder.message.create.embed
import dev.kord.rest.builder.message.modify.embed
import dev.kord.x.emoji.Emojis
import extensions.voicestateupdate.Action
import org.koin.core.component.inject

class ConfigureExtension: Extension() {
    override val name = "configure"

    private val messageCommandsRegistry: ChatCommandRegistry by inject()

    override suspend fun setup() {
        ephemeralSlashCommand {
            name = "configure"
            description = "Configure the server's current preferences"

            check { isNotBot() }

            ephemeralSubCommand {
                name = "view"
                description = "View the server's current preferences"

                action {
                    respond {
                        guild?.let { guild ->
                            embed {
                                color = Color(EnvironmentVariables.accentColor()[0], EnvironmentVariables.accentColor()[1], EnvironmentVariables.accentColor()[2])
                                title = guild.asGuild().name
                                field("Notifications Channel") {
                                    if (Database.get(guild).channelId != null) {
                                        Database.get(guild).channelId?.let(::Snowflake)?.let { this@ConfigureExtension.kord.getChannel(it)?.mention } ?: "No channel set"
                                    } else {
                                        "No channel set"
                                    }
                                }
                                for (actionItem in actionList) {
                                    field(actionItem.name.lowercase().replaceFirstChar(Char::titlecase)) {
                                        if (getActionToggle(actionItem, guild)) {
                                            Emojis.whiteCheckMark.unicode
                                        } else {
                                            Emojis.x.unicode
                                        }
                                    }
                                }
                                field("Subcommands:") {
                                    var subCommandsNames = ""
                                    for (chatCommand in messageCommandsRegistry.commands) {
                                        if (chatCommand is ChatGroupCommand && chatCommand.name == this@ConfigureExtension.name) {
                                            for (command in chatCommand.commands) {
                                                subCommandsNames += if (command != chatCommand.commands.last()) "`${command.name}`, " else "`${command.name}`"
                                            }
                                        }
                                    }
                                    subCommandsNames
                                }
                            }
                        }
                    }
                }
            }

            ephemeralSubCommand {
                name = "notifications"
                description = "Configure which events should trigger a notification"

                check { hasPermission(Permission.ManageGuild) }

                action {
                    guild?.let { guild ->
                        respond {
                            content = "I have sent you the configuration options for this server via DM."
                        }
                        member?.getDmChannel()?.createMessage {
                            embed {
                                author {
                                    name = "${guild.asGuild().name} configuration"
                                    icon = guild.asGuild().icon?.cdnUrl?.toUrl { format = Image.Format.PNG }
                                }
                                color = Color(EnvironmentVariables.accentColor()[0], EnvironmentVariables.accentColor()[1], EnvironmentVariables.accentColor()[2])
                                for (action in actionList) {
                                    field("${action.name.lowercase().replaceFirstChar(Char::titlecase)} ${if (getActionToggle(action, guild)) Emojis.whiteCheckMark.unicode else Emojis.x.unicode}")
                                }
                            }
                            components {
                                if (member != null) {
                                    for (action in actionList) {
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
                            content = if (Database.get(it).channelId != arguments.scope.id.toString()) {
                                Database.updateChannel(it, arguments.scope)
                                "${
                                    if (Database.get(it).channelId == arguments.scope.id.toString()) "Successfully" 
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
                Database.update(guild, action, !getActionToggle(action, guild))
                edit {
                    embed {
                        author {
                            name = "${guild.asGuild().name} server configuration"
                            icon = guild.asGuild().icon?.cdnUrl?.toUrl { format = Image.Format.PNG }
                        }
                        color = Color(EnvironmentVariables.accentColor()[0], EnvironmentVariables.accentColor()[1], EnvironmentVariables.accentColor()[2])
                        for (actionItem in actionList) {
                            field(
                                buildString {
                                    append(actionItem.name.lowercase().replaceFirstChar(Char::titlecase))
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

    private val actionList = setOf(
        Action.JOIN,
        Action.LEAVE,
        Action.SWITCH,
        Action.STREAM,
        Action.VIDEO
    )

    private suspend fun getActionToggle(action: Action, guild: GuildBehavior): Boolean {
        val guildPrefs = Database.get(guild)
        return when (action) {
            Action.JOIN -> guildPrefs.join
            Action.LEAVE -> guildPrefs.leave
            Action.SWITCH -> guildPrefs.switch
            Action.STREAM -> guildPrefs.stream
            Action.VIDEO -> guildPrefs.video
            Action.UNKNOWN -> error("Unknown")
        }
    }
}
