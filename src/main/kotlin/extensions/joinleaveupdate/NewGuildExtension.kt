package extensions.joinleaveupdate

import com.kotlindiscord.kord.extensions.extensions.Extension
import com.kotlindiscord.kord.extensions.extensions.event
import data.Datastore
import dev.kord.common.entity.ChannelType
import dev.kord.core.behavior.channel.MessageChannelBehavior
import dev.kord.core.event.guild.GuildCreateEvent
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.first

class NewGuildExtension: Extension() {

    override val name = "new-guild"

    override suspend fun setup() {
        event<GuildCreateEvent> {
            action {
                if (Datastore.GuildPrefsCollection.isNewGuild(event.guild)) {
                    Datastore.GuildPrefsCollection.createGuild(event.guild)
                    val topTextChannel = event.guild.channels.filter { it.type == ChannelType.GuildText }.first()
                    Datastore.GuildPrefsCollection.updateChannel(event.guild, topTextChannel)
                    MessageChannelBehavior(topTextChannel.id, kord).createMessage("Thanks for inviting me! For now, I have set ${topTextChannel.mention} as the text channel for voice state notifications. This can be configured with `/configure channel`.")
                }
            }
        }
    }
}