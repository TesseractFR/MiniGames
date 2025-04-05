package onl.tesseract.miniGames.command.arguments

import onl.tesseract.commandBuilder.CommandArgument
import onl.tesseract.commandBuilder.CommandArgumentBuilderSteps
import onl.tesseract.miniGames.MiniGamesPlugin
import onl.tesseract.miniGames.minigames.MiniGame

class MiniGameArgument(name: String) : CommandArgument<MiniGame>(name) {
    override fun define(builder: CommandArgumentBuilderSteps.Parser<MiniGame>) {
        builder.parser { input, _ ->
            MiniGamesPlugin.instance.miniGames[input]
        }
                .tabCompleter { input, _ ->
                    MiniGamesPlugin.instance.miniGames.filter { it.key.startsWith(input) }.keys.toList()
                }
                .errorHandler(IllegalArgumentException::class.java, "Minigames invalide")
    }
}