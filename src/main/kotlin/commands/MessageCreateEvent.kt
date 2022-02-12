package commands

import dev.kord.core.event.message.MessageCreateEvent
import dev.kord.core.on
import io.klogging.Klogging
import kord

object MessageCreateEvent: Klogging {

    suspend fun listener() {
        kord.on<MessageCreateEvent> {
            val member = member ?: return@on
            if (member.isBot) return@on
            if (message.content == Command.Notify.actionName) NotifyCommand.run(member, message)
        }
    }

}