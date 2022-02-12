package commands

/**
 * Enum class for all commands
 *
 * @param actionName [String] - The command trigger
 */
enum class Command(val actionName: String) {
    Notify("/notify"),
}