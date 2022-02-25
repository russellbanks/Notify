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

import Config
import com.kotlindiscord.kord.extensions.checks.isNotBot
import com.kotlindiscord.kord.extensions.components.ComponentContainer
import com.kotlindiscord.kord.extensions.components.publicButton
import com.kotlindiscord.kord.extensions.components.types.emoji
import com.kotlindiscord.kord.extensions.extensions.Extension
import com.kotlindiscord.kord.extensions.types.edit
import data.Datastore
import dev.kord.common.Color
import dev.kord.common.entity.ButtonStyle
import dev.kord.common.entity.Snowflake
import dev.kord.rest.builder.message.modify.embed
import dev.kord.x.emoji.Emojis
import extensions.voicestateupdate.Action
import io.github.qbosst.kordex.builders.embed
import io.github.qbosst.kordex.commands.hybrid.publicHybridCommand
import io.github.qbosst.kordex.components.components

class ConfigureInteraction: Extension() {
    override val name = "Configure"

    override suspend fun setup() {
        publicHybridCommand {
            name = "Configure"
            description = "Configure a server's preferences"

            check { isNotBot() }

            action {
                respond {
                    embed {
                        color = Color(Config.accentColor()[0], Config.accentColor()[1], Config.accentColor()[2])
                        title = "${member?.getGuild()?.name} server configuration"
                        actionList().forEach { action ->
                            field("${action.name.lowercase().replaceFirstChar { it.titlecase()} } ${if (getActionToggle(action, member?.guildId!!)) Emojis.whiteCheckMark.unicode else Emojis.x.unicode}")
                        }
                    }
                    components {
                        member?.let {
                            actionList().forEach { action ->
                                publicActionButton(action, it.guildId)
                            }
                        }
                    }
                }
            }
        }
    }

    private suspend fun ComponentContainer.publicActionButton(action: Action, guildId: Snowflake) {
        publicButton {
            label = action.name.lowercase().replaceFirstChar { it.titlecase() }
            style = ButtonStyle.Secondary
            emoji(action.emoji.unicode)
            deferredAck = true
            action {
                Datastore.GuildPrefsCollection.update(guildId, action, !getActionToggle(action, guildId))
                edit {
                    this.embed {
                        color = Color(Config.accentColor()[0], Config.accentColor()[1], Config.accentColor()[2])
                        title = "${member?.getGuild()?.name} server configuration"
                        actionList().forEach { action ->
                            field("${action.name.lowercase().replaceFirstChar { it.titlecase()} } ${if (getActionToggle(action, guildId)) Emojis.whiteCheckMark.unicode else Emojis.x.unicode}")
                        }
                    }
                }
            }
        }
    }

    private fun actionList() = listOf(
        Action.JOIN,
        Action.LEAVE,
        Action.SWITCH,
        Action.STREAM,
        Action.VIDEO
    )

    private suspend fun getActionToggle(action: Action, guildId: Snowflake): Boolean {
        val guildPrefs = Datastore.GuildPrefsCollection.get(guildId)
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