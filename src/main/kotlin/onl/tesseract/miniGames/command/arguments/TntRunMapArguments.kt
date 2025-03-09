package onl.tesseract.miniGames.command.arguments

import onl.tesseract.commandBuilder.CommandArgument
import onl.tesseract.commandBuilder.CommandArgumentBuilderSteps
import onl.tesseract.miniGames.MiniGamesPlugin
import onl.tesseract.miniGames.TNT_RUN_GAMES
import onl.tesseract.miniGames.minigames.tntrun.TntRunMap

class TntRunMapArguments(name: String) : CommandArgument<TntRunMap>(name) {
    override fun define(builder: CommandArgumentBuilderSteps.Parser<TntRunMap>) {
        builder.parser { input, _ ->
            MiniGamesPlugin.instance.miniGames[TNT_RUN_GAMES]!!.maps[input] as TntRunMap
        }
                .tabCompleter { input, env ->
                    MiniGamesPlugin.instance.miniGames[TNT_RUN_GAMES]!!.maps
                            .filter { it.key.startsWith(input) }.keys.toList()
                }
                .errorHandler(IllegalArgumentException::class.java, "Maps invalide")
    }
}