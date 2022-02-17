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

package interactions

import data.Datastore
import dev.kord.core.behavior.interaction.followUpEphemeral
import dev.kord.core.entity.interaction.ChatInputCommandInvocationInteraction

class ConfigureInteraction(override val name: String = "configure") : InteractionCommand() {

    override suspend fun main(interaction: ChatInputCommandInvocationInteraction) {
        val guildId = interaction.data.guildId.value?.value.toString()
        val feature = interaction.data.data.options.value?.get(0)?.value?.value?.value.toString()
        val toggle = interaction.data.data.options.value?.get(1)?.value?.value?.value.toString().toBool()

        Datastore.GuildPrefsCollection.update(guildId, feature, toggle)

        interaction.acknowledgeEphemeral().followUpEphemeral {
            content = "Updated"
        }
    }

    /**
     * Converts a String to a Boolean based on "yes", "on" or "true"
     *
     * @return [Boolean]
     */
    private fun String.toBool(): Boolean {
        return when(this) {
            "yes", "on", "true" -> true
            else -> false
        }
    }

}