import dev.kord.core.Kord
import dev.kord.core.event.interaction.ChatInputCommandInteractionCreateEvent
import dev.kord.core.event.user.VoiceStateUpdateEvent
import dev.kord.core.on
import dev.kord.gateway.Intent
import dev.kord.gateway.Intents
import interactions.InteractionCommand
import io.klogging.Klogging
import vcStateChange.voiceStateChange

class Bot: Klogging {

    lateinit var client: Kord

    suspend fun init(): Bot {
        client = Kord(Config.discordApiKey)
        return this
    }

    suspend fun listenInteractions(vararg interactions: InteractionCommand) {
        client.on<ChatInputCommandInteractionCreateEvent> {
            // Find the chosen interaction from the provided list
            val chosen = arrayOf(*interactions).find { ic -> ic.name == interaction.name }

            // If interaction exists call it
            if(chosen != null) chosen.call(interaction)
            else logger.error("Interaction ${interaction.name} not found")
        }
    }

    suspend fun listenVoiceState() {
        client.on<VoiceStateUpdateEvent> {
            runCatching {
                 voiceStateChange(this)
            }.onSuccess {
                logger.info("[VoiceStateChange] Executed successfully")
            }.onFailure {
                logger.error("[VoiceStateChange] ${it.message.toString()}")
            }
        }
    }

    suspend fun login(vararg intent: Intent) {
        client.login {
            intents = Intents.nonPrivileged + Intents(*intent)
            presence {
                playing(Config.url)
            }
        }
    }

    companion object {

        suspend fun create(): Bot = Bot().init()

    }

}