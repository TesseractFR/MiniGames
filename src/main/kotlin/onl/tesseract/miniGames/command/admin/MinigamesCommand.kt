package onl.tesseract.miniGames.command.admin

import onl.tesseract.commandBuilder.annotation.Command
import onl.tesseract.miniGames.MiniGamesPlugin
import org.bukkit.entity.Player

@Command(
    name = "minigames",
    subCommands = [TntRunCommand::class]
)
class MinigamesCommand {
    @Command(playerOnly = true)
    fun listCommand(sender:Player){
        sender.sendMessage(MiniGamesPlugin.instance.miniGames.entries.toList().joinToString { "$it " })
    }

}