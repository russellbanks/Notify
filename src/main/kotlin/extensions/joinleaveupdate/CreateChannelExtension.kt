package extensions.joinleaveupdate

import com.kotlindiscord.kord.extensions.extensions.Extension
import com.kotlindiscord.kord.extensions.extensions.event
import data.DataStore
import dev.kord.common.entity.ChannelType
import dev.kord.core.event.channel.TextChannelCreateEvent
import kotlinx.coroutines.flow.count

class CreateChannelExtension: Extension() {

    override val name = "channel-create-event"

    override suspend fun setup() {
        event<TextChannelCreateEvent> {
            action {
                event.channel.data.guildId.value?.let { kord.getGuild(it) }?.let { guild ->
                    if (guild.channels.count { it.type == ChannelType.GuildText } == 1) {
                        DataStore.GuildPrefsCollection.updateChannel(guild, event.channel)
                    }
                }
            }
        }
    }
}