package onl.tesseract.miniGames.command.arguments

import onl.tesseract.commandBuilder.CommandArgument
import onl.tesseract.commandBuilder.CommandArgumentBuilderSteps
import onl.tesseract.miniGames.MiniGamesPlugin
import onl.tesseract.miniGames.SUMO_GAMES
import onl.tesseract.miniGames.minigames.sumo.SumoMap

class SumoMapArguments(name: String) : CommandArgument<SumoMap>(name) {
    override fun define(builder: CommandArgumentBuilderSteps.Parser<SumoMap>) {
        builder.parser { input, _ ->
            MiniGamesPlugin.instance.miniGames[SUMO_GAMES]!!.maps[input] as SumoMap
        }
                .tabCompleter { input, env ->
                    MiniGamesPlugin.instance.miniGames[SUMO_GAMES]!!.maps
                            .filter { it.key.startsWith(input) }.keys.toList()
                }
                .errorHandler(IllegalArgumentException::class.java, "Maps invalide")
    }
}