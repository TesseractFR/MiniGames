package onl.tesseract.miniGames.utils.enums

enum class ArenaState(val available: Boolean) {
    WAITING(true),
    STARTING(true),
    INGAME(false),
    ENDING(false),
    DISABLED(false);

    var value : String ?= null
}