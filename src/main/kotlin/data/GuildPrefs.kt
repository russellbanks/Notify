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

package data

data class GuildPrefs(
    val guildId: String,
    val channelId: String,
    val join: Boolean,
    val switch: Boolean,
    val leave: Boolean,
    val stream: Boolean,
    val video: Boolean
) {

    override fun toString(): String {
        return """{}"""
    }

}