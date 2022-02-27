package extensions.joinleaveupdate

import com.kotlindiscord.kord.extensions.extensions.Extension
import com.kotlindiscord.kord.extensions.extensions.event
import data.Datastore
import dev.kord.core.event.guild.GuildCreateEvent
import dev.kord.core.event.guild.GuildDeleteEvent

class LeaveGuild: Extension() {

    override val name: String
        get() = "LeaveGuild"

    override suspend fun setup() {
        event<GuildDeleteEvent> {
            action {
                event.guild?.id?.let { Datastore.GuildPrefsCollection.deleteGuild(it) }
            }
        }
    }
}