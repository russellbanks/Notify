package configureInteraction

import db.Connection
import dev.kord.core.behavior.interaction.followUpEphemeral
import dev.kord.core.entity.interaction.ChatInputCommandInvocationInteraction

suspend fun configureInteraction(interaction: ChatInputCommandInvocationInteraction) {

    val guildId = interaction.data.guildId.value?.value.toString()

    val feature = interaction.data.data.options.value?.get(0)?.value?.value?.value.toString()
    val toggle = interaction.data.data.options.value?.get(1)?.value?.value?.value.toString().toBool()

    Connection.updateGuildPrefs(guildId, feature, toggle)

    interaction.acknowledgeEphemeral().followUpEphemeral {
        content = "Updated"
    }

}

private fun String.toBool(): Boolean {
    return when(this) {
        "yes", "on" -> true
        else -> false
    }
}