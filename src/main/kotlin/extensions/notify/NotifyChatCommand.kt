package extensions.notify

import com.kotlindiscord.kord.extensions.checks.hasPermission
import com.kotlindiscord.kord.extensions.checks.isNotBot
import com.kotlindiscord.kord.extensions.extensions.Extension
import com.kotlindiscord.kord.extensions.extensions.chatGroupCommand
import com.kotlindiscord.kord.extensions.utils.respond
import dev.kord.common.entity.Permission
import dev.kord.core.entity.Member

class NotifyChatCommand: Extension() {
    override val name = "NotifyChat"

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
                    message.respond {
                        content = NotifyReply.getNotifyReply(member as Member, NotifyTarget.HERE)
                    }
                }
            }

            chatCommand {
                name = "everyone"
                description = "Notify everyone"

                action {
                    message.respond {
                        content = NotifyReply.getNotifyReply(member as Member, NotifyTarget.EVERYONE)
                    }
                }
            }
        }
    }
}