/**

Notify
Copyright (C) 2022  BanDev

This program is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program.  If not, see <https://www.gnu.org/licenses/>.

 */

import dev.kord.core.Kord
import dev.kord.core.event.interaction.ChatInputCommandInteractionCreateEvent
import dev.kord.core.event.message.MessageCreateEvent
import dev.kord.core.event.user.VoiceStateUpdateEvent
import dev.kord.core.on
import dev.kord.gateway.Intent
import dev.kord.gateway.Intents
import interactions.InteractionCommand
import io.klogging.Klogging
import vcStateChange.StateChange

class Bot: Klogging {

    lateinit var client: Kord

    suspend fun init(): Bot {
        client = Kord(Config.discordApiKey)
        return this
    }

    suspend fun listenInteractions(vararg interactions: InteractionCommand) {
        client.on<ChatInputCommandInteractionCreateEvent> {
            val chosen = arrayOf(*interactions).find { interactionCommand -> interactionCommand.name == interaction.name }

            if (chosen != null) chosen.call(interaction)
            else logger.error("Interaction ${interaction.name} not found")
        }
    }

    suspend fun listenVoiceState() {
        client.on<VoiceStateUpdateEvent> {
            runCatching { StateChange.voiceStateChange(this) }
                .onSuccess { logger.info("[VoiceStateChange] Executed successfully") }
                .onFailure { logger.error("[VoiceStateChange] ${it.message.toString()}") }
        }
    }

    suspend fun listenMessage() {
        client.on<MessageCreateEvent> {
            val member = member ?: return@on
            if (message.content == "/notify") FallbackNotifyCommand.sendReply(member, message)
        }
    }

    suspend fun login(vararg intent: Intent) {
        client.login {
            intents = Intents.nonPrivileged + Intents(*intent)
            presence {
                playing(Config.url)
            }
        }
    }

    companion object {
        suspend fun create(): Bot = Bot().init()
    }

}