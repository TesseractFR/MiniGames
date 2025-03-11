package onl.tesseract.miniGames.command

import onl.tesseract.commandBuilder.CommandContext
import onl.tesseract.commandBuilder.annotation.Command
import onl.tesseract.miniGames.command.basic.JoinCommand
import onl.tesseract.miniGames.command.basic.TntRunCommand

@Command(
    name = "minigames",
    subCommands = [TntRunCommand::class]
)
class MinigamesCommand : CommandContext(){
}