package onl.tesseract.miniGames.command.basic

import net.kyori.adventure.text.Component
import onl.tesseract.commandBuilder.annotation.Argument
import onl.tesseract.commandBuilder.annotation.Command
import onl.tesseract.lib.command.argument.IntegerCommandArgument
import onl.tesseract.lib.command.argument.StringArg
import onl.tesseract.miniGames.MiniGamesPlugin
import onl.tesseract.miniGames.TNT_RUN_GAMES
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