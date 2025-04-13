package onl.tesseract.miniGames.command.arguments

import onl.tesseract.commandBuilder.CommandArgument
import onl.tesseract.commandBuilder.CommandArgumentBuilderSteps
import onl.tesseract.miniGames.minigames.MiniGameMap

class MiniGamesMapArgument(name: String) : CommandArgument<MiniGameMap>(name) {


    override fun define(builder: CommandArgumentBuilderSteps.Parser<MiniGameMap>) {
        builder.parser { input, env ->
            (env.get("minigame") as MiniGameArgument).get().maps[input]
        }
                .tabCompleter { input, env ->
                    (env.get("minigame") as MiniGameArgument).get().maps
                            .filter { it.key.startsWith(input) }.keys.toList()
                }
                .errorHandler(IllegalArgumentException::class.java, "Maps invalide")
    }
}