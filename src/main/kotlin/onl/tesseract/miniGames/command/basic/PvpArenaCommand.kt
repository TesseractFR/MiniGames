package onl.tesseract.miniGames.command.basic

import onl.tesseract.commandBuilder.annotation.Argument
import onl.tesseract.commandBuilder.annotation.Command
import onl.tesseract.miniGames.command.arguments.PvpArenaMapArguments
import onl.tesseract.miniGames.minigames.pvparena.PvpArenaMap
import org.bukkit.entity.Player

@Command(
    name = "PvpArena"
)
class PvpArenaCommand {

    @Command
    fun joinCommand(@Argument(value = "map", clazz = PvpArenaMapArguments::class) map:PvpArenaMap,
                    sender: Player) {
        map.join(sender)
    }
}