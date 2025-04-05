package onl.tesseract.miniGames.command

import onl.tesseract.commandBuilder.CommandContext
import onl.tesseract.commandBuilder.annotation.Argument
import onl.tesseract.commandBuilder.annotation.Command
import onl.tesseract.miniGames.command.arguments.MiniGameArgument
import onl.tesseract.miniGames.command.arguments.MiniGamesMapArgument
import onl.tesseract.miniGames.minigames.MiniGame
import onl.tesseract.miniGames.minigames.MiniGameMap
import org.bukkit.entity.Player

@Command(
    name = "minigames",
)
class MinigamesCommand : CommandContext(){
    @Command
    fun joinCommand(@Argument(value = "minigame", clazz = MiniGameArgument::class) minigame : MiniGame,
                    @Argument(value = "arena", clazz = MiniGamesMapArgument::class) map: MiniGameMap,
                    sender: Player
    ) {
        map.join(sender)
    }
}