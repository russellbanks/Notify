package extensions.joinleaveupdate

import com.kotlindiscord.kord.extensions.extensions.Extension
import com.kotlindiscord.kord.extensions.extensions.event
import data.Datastore
import dev.kord.common.entity.ChannelType
import dev.kord.core.event.channel.TextChannelDeleteEvent
import kotlinx.coroutines.flow.count
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.first

class ChannelDeleteEventExtension: Extension() {

    override val name = "channel-delete-event"

    override suspend fun setup() {
        event<TextChannelDeleteEvent> {
            action {
                event.channel.data.guildId.value?.let { kord.getGuild(it) }?.let { guild ->
                    if (guild.channels.count { it.type == ChannelType.GuildText } == 0) {
                        Datastore.GuildPrefsCollection.updateChannel(guild, null)
                    } else if (Datastore.GuildPrefsCollection.get(guild).channelId == event.channel.id.toString()) {
                        Datastore.GuildPrefsCollection.updateChannel(guild, guild.channels.filter { it.type == ChannelType.GuildText }.first())
                    }
                }
            }
        }
    }
}