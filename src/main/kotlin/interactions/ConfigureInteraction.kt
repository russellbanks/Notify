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