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

import extensions.notify.NotifyCommand
import extensions.voicestateupdate.VoiceStateUpdate
import com.kotlindiscord.kord.extensions.ExtensibleBot
import data.Datastore
import dev.kord.gateway.Intent
import dev.kord.gateway.Intents
import dev.kord.gateway.PrivilegedIntent
import extensions.ConfigureInteraction

@OptIn(PrivilegedIntent::class)
suspend fun main() {

    Datastore.GuildPrefsCollection.setupCache()

    ExtensibleBot(Config.discordApiKey) {
        chatCommands {
            defaultPrefix = "/"
            enabled = true
            invokeOnMention = true
        }

        intents {
            +Intents.nonPrivileged
            +Intent.Guilds
            +Intent.GuildVoiceStates
            +Intent.GuildMembers
            +Intent.DirectMessages
        }

        applicationCommands {
            enabled = true
            defaultGuild(Config.defaultGuildID)
        }

        presence {
            playing(Config.playing)
        }

        extensions {
            add(::NotifyCommand)
            add(::ConfigureInteraction)
            add(::VoiceStateUpdate)

            help {
                pingInReply = false
            }
        }
    }.start()
}