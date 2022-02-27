package extensions.joinleaveupdate

import com.kotlindiscord.kord.extensions.extensions.Extension
import com.kotlindiscord.kord.extensions.extensions.event
import data.Datastore
import dev.kord.core.event.guild.GuildCreateEvent

class NewGuild: Extension() {

    override val name: String
        get() = "NewGuild"

    override suspend fun setup() {
        event<GuildCreateEvent> {
            action {
                if (Datastore.GuildPrefsCollection.isNewGuild(event.guild.id)) {
                    Datastore.GuildPrefsCollection.createGuild(event.guild.id)
                }
            }
        }
    }
}