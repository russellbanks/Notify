package extensions.notify

import com.kotlindiscord.kord.extensions.commands.application.slash.converters.ChoiceEnum

enum class NotifyTarget(override val readableName: String): ChoiceEnum {
    HERE("here"),
    EVERYONE("everyone")
}
