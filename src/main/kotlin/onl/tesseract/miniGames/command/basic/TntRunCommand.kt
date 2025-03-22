package onl.tesseract.miniGames.command.basic

import onl.tesseract.commandBuilder.annotation.Argument
import onl.tesseract.commandBuilder.annotation.Command
import onl.tesseract.miniGames.command.arguments.TntRunMapArguments
import onl.tesseract.miniGames.minigames.tntrun.TntRunMap
import org.bukkit.entity.Player

@Command(
    name = "tntgames"
)
class TntRunCommand {

    @Command
    fun joinCommand(@Argument(value = "map", clazz = TntRunMapArguments::class) map:TntRunMap,
                    sender: Player) {
        map.join(sender)
    }
}