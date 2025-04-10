package onl.tesseract.miniGames.command.arguments

import onl.tesseract.commandBuilder.CommandArgument
import onl.tesseract.commandBuilder.CommandArgumentBuilderSteps
import org.bukkit.Bukkit
import org.bukkit.entity.Player

class OnlinePlayerArg (name: String) : CommandArgument<Player>(name) {
    override fun define(builder: CommandArgumentBuilderSteps.Parser<Player>) {
        builder.parser { input, _ ->
            Bukkit.getPlayer(input)
        }
                .tabCompleter { input, _ ->
                    Bukkit.getOnlinePlayers().filter { it.name.startsWith(input) }.map { it.name}.toList()
                }
                .errorHandler(IllegalArgumentException::class.java, "Joueur invalide")
    }
}