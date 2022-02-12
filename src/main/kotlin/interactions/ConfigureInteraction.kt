package interactions

import data.Datastore
import dev.kord.core.behavior.interaction.followUpEphemeral
import dev.kord.core.entity.interaction.ChatInputCommandInvocationInteraction

class ConfigureInteraction(override val name: String = "configure") : InteractionCommand() {

    override suspend fun main(interaction: ChatInputCommandInvocationInteraction) {
        // Grab all information from the interaction
        val guildId = interaction.data.guildId.value?.value.toString()
        val feature = interaction.data.data.options.value?.get(0)?.value?.value?.value.toString()
        val toggle = interaction.data.data.options.value?.get(1)?.value?.value?.value.toString().toBool()

        // Update the configuration in the db
        Datastore.GuildPrefsCollection.update(guildId, feature, toggle)

        // Acknowledge changes to the user
        interaction.acknowledgeEphemeral().followUpEphemeral {
            content = "Updated"
        }
    }

    /**
     * Convert a string to a boolean
     * based on if it is yes, on, true etc
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