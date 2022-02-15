package vcStateChange

import dev.kord.x.emoji.Emojis

/**
 * Enum class for all state change actions
 *
 * @param text [String] - The english text for the action
 * @param emojiUnicode [String] - The emoji's unicode for the action
 */

enum class Action(val text: String, val emojiUnicode: String) {
    JOIN("joined", Emojis.headphones.unicode),
    SWITCH("switched to", Emojis.repeat.unicode),
    LEAVE("left", Emojis.door.unicode),
    STREAM("is live in", Emojis.redCircle.unicode),
    VIDEO("turned their video on in", Emojis.camera.unicode)
}