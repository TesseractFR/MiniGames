package onl.tesseract.miniGames.command

import onl.tesseract.commandBuilder.CommandContext
import onl.tesseract.commandBuilder.annotation.Command
import onl.tesseract.miniGames.MiniGamesPlugin
import onl.tesseract.miniGames.command.admin.*

@Command(
    name = "minigamesadmin",
    subCommands = [TntRunCommand::class, SumoCommand::class,PvpArenaCommand::class],
)
class MinigamesAdminCommand :CommandContext() {
    @Command
    fun resetAllCommand(){
        for(game in MiniGamesPlugin.instance.miniGames.values){
            for (map in game.maps.values){
                map.resetArena()
            }
        }
    }
}