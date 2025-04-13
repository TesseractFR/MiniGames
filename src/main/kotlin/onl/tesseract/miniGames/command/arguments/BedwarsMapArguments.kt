package onl.tesseract.miniGames.command.arguments

import onl.tesseract.commandBuilder.CommandArgument
import onl.tesseract.commandBuilder.CommandArgumentBuilderSteps
import onl.tesseract.miniGames.BEDWARS_GAMES
import onl.tesseract.miniGames.MiniGamesPlugin
import onl.tesseract.miniGames.minigames.bedwars.BedwarsMap

class BedwarsMapArguments(name: String) : CommandArgument<BedwarsMap>(name) {
    override fun define(builder: CommandArgumentBuilderSteps.Parser<BedwarsMap>) {
        builder.parser { input, _ ->
            MiniGamesPlugin.instance.miniGames[BEDWARS_GAMES]!!.maps[input] as BedwarsMap
        }
                .tabCompleter { input, env ->
                    MiniGamesPlugin.instance.miniGames[BEDWARS_GAMES]!!.maps
                            .filter { it.key.startsWith(input) }.keys.toList()
                }
                .errorHandler(IllegalArgumentException::class.java, "Maps invalide")
    }
}