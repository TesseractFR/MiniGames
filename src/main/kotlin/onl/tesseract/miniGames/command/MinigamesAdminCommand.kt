package onl.tesseract.miniGames.command

import onl.tesseract.commandBuilder.CommandContext
import onl.tesseract.commandBuilder.annotation.Command
import onl.tesseract.miniGames.command.admin.MinigamesCommand

@Command(
    name = "minigamesadmin",
    subCommands = [MinigamesCommand::class]
)
class MinigamesAdminCommand :CommandContext() {
}