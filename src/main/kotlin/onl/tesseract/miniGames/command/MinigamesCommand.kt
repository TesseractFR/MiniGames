package onl.tesseract.miniGames.command

import onl.tesseract.commandBuilder.CommandContext
import onl.tesseract.commandBuilder.annotation.Command
import onl.tesseract.miniGames.command.basic.JoinCommand

@Command(
    name = "minigames",
    subCommands = [JoinCommand::class]
)
class MinigamesCommand : CommandContext(){
}