package onl.tesseract.miniGames.command.basic

import onl.tesseract.commandBuilder.CommandContext
import onl.tesseract.commandBuilder.annotation.Command
import onl.tesseract.commandBuilder.annotation.CommandBody
import onl.tesseract.miniGames.MiniGamesPlugin
import org.bukkit.entity.Player

@Command(name = "join")
class JoinCommand : CommandContext(){
    @CommandBody
    fun command(sender: Player) {
        if(MiniGamesPlugin.instance.joinMiniGames(sender)){
            return
        }
    }
}