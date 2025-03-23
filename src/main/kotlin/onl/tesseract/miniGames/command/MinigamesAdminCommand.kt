package onl.tesseract.miniGames.command

import onl.tesseract.commandBuilder.CommandContext
import onl.tesseract.commandBuilder.annotation.Command
import onl.tesseract.miniGames.command.admin.*

@Command(
    name = "minigamesadmin",
    subCommands = [TntRunCommand::class, SumoCommand::class],
)
class MinigamesAdminCommand :CommandContext() {
}