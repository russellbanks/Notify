package extensions.joinleaveupdate

import com.kotlindiscord.kord.extensions.extensions.Extension
import com.kotlindiscord.kord.extensions.extensions.event
import data.Datastore
import dev.kord.core.event.channel.ChannelDeleteEvent
import dev.kord.core.event.channel.TextChannelDeleteEvent
import dev.kord.core.kordLogger

class ChannelDeleteEventExtension: Extension() {

    override val name = "channel-delete-event"

    override suspend fun setup() {
        event<TextChannelDeleteEvent> {
            action {
                val guild = event.channel.data.guildId.value?.let { kord.getGuild(it) }
                if (guild?.let { Datastore.GuildPrefsCollection.get(it).channelId } == event.channel.id.toString()) {
                    Datastore.GuildPrefsCollection.updateChannel(guild, null)
                }
            }
        }
    }
}