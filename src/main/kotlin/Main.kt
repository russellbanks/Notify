import configureInteraction.configureInteraction
import dev.kord.core.Kord
import dev.kord.core.event.interaction.ChatInputCommandInteractionCreateEvent
import dev.kord.core.event.interaction.InteractionCreateEvent
import dev.kord.core.on
import dev.kord.gateway.Intent
import dev.kord.gateway.Intents
import dev.kord.gateway.PrivilegedIntent
import io.klogging.config.DEFAULT_CONSOLE
import io.klogging.config.loggingConfiguration
import notifyCommand.MessageCreateEvent
import vcStateChange.VoiceStateUpdateEvent
import java.security.Security

lateinit var kord: Kord

@OptIn(PrivilegedIntent::class)
suspend fun main() {
    loggingConfiguration { DEFAULT_CONSOLE() }

    // Set security properties, this stops warning message from okhttp
    System.setProperty("io.ktor.random.secure.random.provider", "DRBG")
    Security.setProperty("securerandom.drbg.config", "HMAC_DRBG,SHA-512,256,pr_and_reseed")

    kord = Kord(Config.discordApiKey)

    VoiceStateUpdateEvent.listener()

    MessageCreateEvent.listener()

    kord.on<ChatInputCommandInteractionCreateEvent>{
        when(interaction.data.data.name.value) {
            "configure" -> configureInteraction(interaction)
        }


        interaction.acknowledgePublic()
    }

    kord.login {
        intents = Intents.nonPrivileged + Intents(Intent.Guilds, Intent.GuildVoiceStates, Intent.GuildMembers, Intent.DirectMessages)
        presence {
            playing("bandev.uk/notify")
        }
    }
}