package extensions.joinleaveupdate

import com.kotlindiscord.kord.extensions.extensions.Extension
import com.kotlindiscord.kord.extensions.extensions.event
import com.kotlindiscord.kord.extensions.utils.scheduling.Scheduler
import data.DataStore
import dev.kord.common.entity.ChannelType
import dev.kord.core.behavior.channel.MessageChannelBehavior
import dev.kord.core.event.guild.GuildCreateEvent
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.first
import kotlin.time.Duration.Companion.days

class NewGuildExtension: Extension() {

    override val name = "new-guild"

    override suspend fun setup() {
        event<GuildCreateEvent> {
            action {
                if (DataStore.GuildPrefsCollection.isNewGuild(event.guild)) {
                    DataStore.GuildPrefsCollection.createGuild(event.guild)
                    val topTextChannel = event.guild.channels.filter { it.type == ChannelType.GuildText }.first()
                    DataStore.GuildPrefsCollection.updateChannel(event.guild, topTextChannel)
                    val joinMessage = MessageChannelBehavior(topTextChannel.id, kord).createMessage("Thanks for inviting me! For now, I have set ${topTextChannel.mention} as the text channel for voice state notifications. This can be configured with `/configure channel`.")
                    Scheduler().schedule(7.days) { joinMessage.delete() }
                }
            }
        }
    }
}