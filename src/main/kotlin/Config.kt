import io.github.cdimascio.dotenv.Dotenv
import io.github.cdimascio.dotenv.dotenv

object Config {

    private val dotenv: Dotenv = dotenv {
        ignoreIfMalformed = true
        ignoreIfMissing = true
    }

    val discordApiKey = dotenv["DISCORD_API_KEY"] ?: error("Token required.")
    val mongoDbUri = dotenv["MONGODB_URI"] ?: error("Mongodb uri required.")
    val url = dotenv["BOT_URL"] ?: "bandev.uk/notify"
}