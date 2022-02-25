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

import dev.kord.x.emoji.DiscordEmoji
import dev.kord.x.emoji.Emojis

/**
 * Enum class for all state change actions
 *
 * @param text [String] - The english text for the action
 * @param emoji [String] - The emoji
 */

enum class Action(val text: String, val emoji: DiscordEmoji.Generic) {
    JOIN("joined", Emojis.headphones),
    LEAVE("left", Emojis.door),
    SWITCH("switched to", Emojis.repeat),
    STREAM("is live in", Emojis.redCircle),
    VIDEO("turned their video on in", Emojis.camera),
    UNKNOWN("unknown", Emojis.greyQuestion)
}