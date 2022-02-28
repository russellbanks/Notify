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

import com.kotlindiscord.kord.extensions.ExtensibleBot
import com.kotlindiscord.kord.extensions.checks.isNotBot
import data.Datastore
import dev.kord.common.entity.Snowflake
import dev.kord.gateway.Intent
import dev.kord.gateway.Intents
import dev.kord.gateway.PrivilegedIntent
import extensions.ConfigureHybridExtension
import extensions.joinleaveupdate.LeaveGuildExtension
import extensions.joinleaveupdate.NewGuildExtension
import extensions.notify.NotifyChatExtension
import extensions.notify.NotifyEphemeralExtension
import extensions.voicestateupdate.VoiceStateExtension

@OptIn(PrivilegedIntent::class)
suspend fun main() {

    Datastore.GuildPrefsCollection.setupCache()

    ExtensibleBot(EnvironmentVariables.discordApiKey) {
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
            defaultGuild(EnvironmentVariables.defaultGuildId?.let { Snowflake(it) })
        }

        cache {
            cachedMessages = null
        }

        presence {
            playing(EnvironmentVariables.playing)
        }

        extensions {
            add(::NotifyEphemeralExtension)
            add(::NotifyChatExtension)
            add(::ConfigureHybridExtension)
            add(::VoiceStateExtension)
            add(::NewGuildExtension)
            add(::LeaveGuildExtension)

            help {
                check { isNotBot() }
                pingInReply = false
            }
        }
    }.start()
}