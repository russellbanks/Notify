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

package vcStateChange

import dev.kord.x.emoji.Emojis

/**
 * Enum class for all state change actions
 *
 * @param text [String] - The english text for the action
 * @param emojiUnicode [String] - The emoji's unicode for the action
 */

enum class Action(val text: String, val emojiUnicode: String) {
    JOIN("joined", Emojis.headphones.unicode),
    SWITCH("switched to", Emojis.repeat.unicode),
    LEAVE("left", Emojis.door.unicode),
    STREAM("is live in", Emojis.redCircle.unicode),
    VIDEO("turned their video on in", Emojis.camera.unicode)
}