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

import com.kotlindiscord.kord.extensions.utils.env
import com.kotlindiscord.kord.extensions.utils.envOrNull

object EnvironmentVariables {

    val discordApiKey = env("DISCORD_API_KEY")
    val mongoDbUri = env("MONGODB_URI")
    val playing = envOrNull("BOT_PLAYING")
    val defaultGuildId = envOrNull("DEFAULT_GUILD_ID")

    fun accentColor(): List<Int> {
        return (envOrNull("BOT_COLOR_HEX") ?: "0067f4")
            .chunked(2)
            .map { it.toInt(16) }
    }

}