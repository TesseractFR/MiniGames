package onl.tesseract.miniGames.command

import onl.tesseract.commandBuilder.CommandContext
import onl.tesseract.commandBuilder.annotation.Argument
import onl.tesseract.commandBuilder.annotation.Command
import onl.tesseract.miniGames.MiniGamesPlugin
import onl.tesseract.miniGames.command.arguments.MiniGameArgument
import onl.tesseract.miniGames.command.arguments.MiniGamesMapArgument
import onl.tesseract.miniGames.minigames.MiniGame
import onl.tesseract.miniGames.minigames.MiniGameMap
import onl.tesseract.miniGames.utils.enums.ArenaState
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
        MiniGamesPlugin.instance.miniGames.values.forEach {
            it.maps.values.forEach{
                if(it.players.contains(sender) || it.spectators.contains(sender)){
                    return
                }
            }
        }
        map.join(sender)
    }

    @Command
    fun specCommand(@Argument(value = "minigame", clazz = MiniGameArgument::class) minigame : MiniGame,
                    @Argument(value = "arena", clazz = MiniGamesMapArgument::class) map: MiniGameMap,
                    sender: Player)
    {
        MiniGamesPlugin.instance.miniGames.values.forEach {
            it.maps.values.forEach{
                if(it.players.contains(sender) || it.spectators.contains(sender)){
                    return
                }
            }
        }
        map.joinSpectator(sender)
    }
    @Command
    fun leaveCommand(@Argument(value = "minigame", clazz = MiniGameArgument::class) minigame : MiniGame,
                    @Argument(value = "arena", clazz = MiniGamesMapArgument::class) map: MiniGameMap,
                    sender: Player)
    {
        map.leave(sender)
    }
}