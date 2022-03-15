package extensions.joinleaveupdate

import com.kotlindiscord.kord.extensions.extensions.Extension
import com.kotlindiscord.kord.extensions.extensions.event
import data.Datastore
import dev.kord.common.entity.ChannelType
import dev.kord.core.event.channel.TextChannelCreateEvent
import kotlinx.coroutines.flow.count
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.first

class ChannelCreateEventExtension: Extension() {

    override val name = "channel-create-event"

    override suspend fun setup() {
        event<TextChannelCreateEvent> {
            action {
                event.channel.data.guildId.value?.let { kord.getGuild(it) }?.let { guild ->
                    if (guild.channels.count { it.type == ChannelType.GuildText } == 1) {
                        Datastore.GuildPrefsCollection.updateChannel(guild, event.channel)
                    }
                }
            }
        }
    }
}