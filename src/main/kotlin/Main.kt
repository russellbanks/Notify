import data.Datastore
import dev.kord.gateway.Intent
import dev.kord.gateway.Intents
import dev.kord.gateway.PrivilegedIntent
import interactions.ConfigureInteraction
import interactions.NotifyInteraction
import io.klogging.config.DEFAULT_CONSOLE
import io.klogging.config.loggingConfiguration
import java.security.Security

lateinit var bot: Bot

@OptIn(PrivilegedIntent::class)
suspend fun main() {
    loggingConfiguration { DEFAULT_CONSOLE() }

    // Set security properties, this stops warning message from okhttp
    System.setProperty("io.ktor.random.secure.random.provider", "DRBG")
    Security.setProperty("securerandom.drbg.config", "HMAC_DRBG,SHA-512,256,pr_and_reseed")

    // Setup cache for guild prefs
    Datastore.GuildPrefsCollection.setupCache()

    // Setup bot
    bot = Bot.create()
    bot.run {
        listenInteractions(NotifyInteraction(), ConfigureInteraction())
        listenVoiceState()
        login(Intent.Guilds, Intent.GuildVoiceStates, Intent.GuildMembers, Intent.DirectMessages)
    }
}