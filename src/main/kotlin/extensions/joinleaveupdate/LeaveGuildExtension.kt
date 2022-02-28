package extensions.joinleaveupdate

import com.kotlindiscord.kord.extensions.extensions.Extension
import com.kotlindiscord.kord.extensions.extensions.event
import data.Datastore
import dev.kord.core.event.guild.GuildDeleteEvent

class LeaveGuildExtension: Extension() {

    override val name = "leave-guild"

    override suspend fun setup() {
        event<GuildDeleteEvent> {
            action {
                event.guild?.id?.let { Datastore.GuildPrefsCollection.deleteGuild(it) }
            }
        }
    }
}