package onl.tesseract.miniGames.command.basic

import onl.tesseract.commandBuilder.annotation.Argument
import onl.tesseract.commandBuilder.annotation.Command
import onl.tesseract.miniGames.command.arguments.SumoMapArguments
import onl.tesseract.miniGames.command.arguments.TntRunMapArguments
import onl.tesseract.miniGames.minigames.sumo.SumoMap
import onl.tesseract.miniGames.minigames.tntrun.TntRunMap
import org.bukkit.entity.Player

@Command(
    name = "sumo"
)
class SumoCommand {

    @Command
    fun joinCommand(@Argument(value = "map", clazz = SumoMapArguments::class) map:SumoMap,
                    sender: Player) {
        map.join(sender)
    }
}