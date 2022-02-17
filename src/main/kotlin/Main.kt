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

import data.Datastore
import dev.kord.gateway.Intent
import dev.kord.gateway.PrivilegedIntent
import interactions.ConfigureInteraction
import interactions.NotifyInteraction
import io.klogging.config.DEFAULT_CONSOLE
import io.klogging.config.loggingConfiguration
import java.security.Security

lateinit var bot: Bot

@OptIn(PrivilegedIntent::class)
suspend fun main() {
    loggingConfiguration { DEFAULT_CONSOLE() }

    // Set security properties, this stops warning message from okhttp
    System.setProperty("io.ktor.random.secure.random.provider", "DRBG")
    Security.setProperty("securerandom.drbg.config", "HMAC_DRBG,SHA-512,256,pr_and_reseed")

    // Setup cache for guild prefs
    Datastore.GuildPrefsCollection.setupCache()

    // Setup bot
    bot = Bot.create()
    bot.run {
        listenInteractions(NotifyInteraction(), ConfigureInteraction())
        listenMessage()
        listenVoiceState()
        login(Intent.Guilds, Intent.GuildVoiceStates, Intent.GuildMembers, Intent.DirectMessages)
    }
}