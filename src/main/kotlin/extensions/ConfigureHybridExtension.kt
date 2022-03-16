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

package extensions

import EnvironmentVariables
import com.kotlindiscord.kord.extensions.checks.hasPermission
import com.kotlindiscord.kord.extensions.checks.isNotBot
import com.kotlindiscord.kord.extensions.commands.Arguments
import com.kotlindiscord.kord.extensions.commands.chat.ChatCommandRegistry
import com.kotlindiscord.kord.extensions.commands.chat.ChatGroupCommand
import com.kotlindiscord.kord.extensions.commands.converters.impl.channel
import com.kotlindiscord.kord.extensions.components.ComponentContainer
import com.kotlindiscord.kord.extensions.components.components
import com.kotlindiscord.kord.extensions.components.publicButton
import com.kotlindiscord.kord.extensions.components.types.emoji
import com.kotlindiscord.kord.extensions.extensions.Extension
import com.kotlindiscord.kord.extensions.types.edit
import data.DataStore
import dev.kord.common.Color
import dev.kord.common.entity.ButtonStyle
import dev.kord.common.entity.ChannelType
import dev.kord.common.entity.Permission
import dev.kord.common.entity.Snowflake
import dev.kord.core.behavior.channel.createMessage
import dev.kord.core.entity.Guild
import dev.kord.rest.Image
import dev.kord.rest.builder.message.create.embed
import dev.kord.rest.builder.message.modify.embed
import dev.kord.x.emoji.Emojis
import extensions.voicestateupdate.Action
import io.github.qbosst.kordex.builders.embed
import io.github.qbosst.kordex.commands.hybrid.ephemeralHybridCommand
import io.github.qbosst.kordex.commands.hybrid.ephemeralSubCommand
import org.koin.core.component.inject

class ConfigureHybridExtension: Extension() {
    override val name = "configure"

    private val messageCommandsRegistry: ChatCommandRegistry by inject()

    override suspend fun setup() {
        ephemeralHybridCommand {
            name = "configure"
            description = "Show the server's current preferences"

            check { isNotBot() }

            slashCommandSettings {
                subCommandName = "view"
            }

            action {
                respond {
                    guild?.let { guild ->
                        embed {
                            color = Color(EnvironmentVariables.accentColor()[0], EnvironmentVariables.accentColor()[1], EnvironmentVariables.accentColor()[2])
                            title = guild.asGuild().name
                            field("Notifications Channel") {
                                if (DataStore.GuildPrefsCollection.get(guild.asGuild()).channelId != null) {
                                    DataStore.GuildPrefsCollection.get(guild.asGuild()).channelId?.let { Snowflake(it) }?.let { kord.getChannel(it)?.mention } ?: "No channel set"
                                } else {
                                    "No channel set"
                                }
                            }
                            actionList.forEach { action ->
                                field(action.name.lowercase().replaceFirstChar { it.titlecase()}) {
                                    if (getActionToggle(action, guild.asGuild())) Emojis.whiteCheckMark.unicode else Emojis.x.unicode
                                }
                            }
                            field("Subcommands:") {
                                var subCommandsNames = ""
                                messageCommandsRegistry.commands.forEach { chatCommand ->
                                    if (chatCommand is ChatGroupCommand && chatCommand.name == name) {
                                        chatCommand.commands.forEach {
                                            subCommandsNames += if (it != chatCommand.commands.last()) "`${it.name}`, " else "`${it.name}`"
                                        }
                                    }
                                }
                                subCommandsNames
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
                                    icon = guild.asGuild().getIconUrl(Image.Format.PNG)
                                }
                                color = Color(EnvironmentVariables.accentColor()[0], EnvironmentVariables.accentColor()[1], EnvironmentVariables.accentColor()[2])
                                actionList.forEach { action ->
                                    field("${action.name.lowercase().replaceFirstChar { it.titlecase()} } ${if (getActionToggle(action, guild.asGuild())) Emojis.whiteCheckMark.unicode else Emojis.x.unicode}")
                                }
                            }
                            components {
                                member?.let {
                                    actionList.forEach { action ->
                                        publicActionButton(action, guild.asGuild())
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
                            content = if (DataStore.GuildPrefsCollection.get(it.asGuild()).channelId != arguments.scope.id.toString()) {
                                DataStore.GuildPrefsCollection.updateChannel(it.asGuild(), arguments.scope)
                                "${if (DataStore.GuildPrefsCollection.get(it.asGuild()).channelId == arguments.scope.id.toString()) "Successfully" else "Failed to"} set ${arguments.scope.mention} as the text channel to send voice state notifications in."
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

    private suspend fun ComponentContainer.publicActionButton(action: Action, guild: Guild) {
        publicButton {
            label = action.name.lowercase().replaceFirstChar { it.titlecase() }
            style = ButtonStyle.Secondary
            emoji(action.emoji.unicode)
            deferredAck = true
            action {
                DataStore.GuildPrefsCollection.update(guild, action, !getActionToggle(action, guild))
                edit {
                    this.embed {
                        author {
                            name = "${guild.name} server configuration"
                            icon = guild.getIconUrl(Image.Format.PNG)
                        }
                        color = Color(EnvironmentVariables.accentColor()[0], EnvironmentVariables.accentColor()[1], EnvironmentVariables.accentColor()[2])
                        actionList.forEach { action ->
                            field("${action.name.lowercase().replaceFirstChar { it.titlecase()} } ${if (getActionToggle(action, guild)) Emojis.whiteCheckMark.unicode else Emojis.x.unicode}")
                        }
                    }
                }
            }
        }
    }

    private val actionList = listOf(
        Action.JOIN,
        Action.LEAVE,
        Action.SWITCH,
        Action.STREAM,
        Action.VIDEO
    )

    private suspend fun getActionToggle(action: Action, guild: Guild): Boolean {
        val guildPrefs = DataStore.GuildPrefsCollection.get(guild)
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