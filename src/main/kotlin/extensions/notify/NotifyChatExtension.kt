package extensions.notify

import com.kotlindiscord.kord.extensions.checks.hasPermission
import com.kotlindiscord.kord.extensions.checks.isNotBot
import com.kotlindiscord.kord.extensions.extensions.Extension
import com.kotlindiscord.kord.extensions.extensions.chatGroupCommand
import com.kotlindiscord.kord.extensions.utils.respond
import com.kotlindiscord.kord.extensions.utils.scheduling.Scheduler
import dev.kord.common.entity.Permission
import dev.kord.core.entity.Member
import kotlin.time.Duration.Companion.hours

class NotifyChatExtension: Extension() {
    override val name = "notify-chat"

    override suspend fun setup() {
        chatGroupCommand {
            name = "Notify"
            description = "Notify"

            check { isNotBot() }
            check { hasPermission(Permission.MentionEveryone) }

            chatCommand {
                name = "here"
                description = "Notify everyone online"

                action {
                    val notifyMessage = message.respond {
                        content = member?.let { NotifyReply.getNotifyReply(it.asMember(), NotifyTarget.HERE) }
                    }
                    Scheduler().schedule(5.hours) {
                        message.delete()
                        notifyMessage.delete()
                    }
                }
            }

            chatCommand {
                name = "everyone"
                description = "Notify everyone"

                action {
                    val notifyMessage = message.respond {
                        content = member?.let { NotifyReply.getNotifyReply(it.asMember(), NotifyTarget.EVERYONE) }
                    }
                    Scheduler().schedule(5.hours) {
                        message.delete()
                        notifyMessage.delete()
                    }
                }
            }
        }
    }
}