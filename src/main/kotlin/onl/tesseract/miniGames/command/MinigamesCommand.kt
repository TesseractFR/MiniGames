package onl.tesseract.miniGames.command

import onl.tesseract.commandBuilder.CommandContext
import onl.tesseract.commandBuilder.annotation.Command
import onl.tesseract.miniGames.command.basic.*

@Command(
    name = "minigames",
    subCommands = [TntRunCommand::class, SumoCommand::class],
)
class MinigamesCommand : CommandContext(){
}