package interactions

import dev.kord.core.entity.interaction.ChatInputCommandInvocationInteraction
import io.klogging.Klogging

abstract class InteractionCommand: Klogging {

    abstract val name: String

    abstract suspend fun main(interaction: ChatInputCommandInvocationInteraction)

    suspend fun call(interaction: ChatInputCommandInvocationInteraction) {
        runCatching { main(interaction) }
            .onSuccess { logger.info("${interaction.name.replaceFirstChar { it.titlecase() }} toggled successfully") }
            .onFailure { logger.error(it.message.toString()) }
    }

}