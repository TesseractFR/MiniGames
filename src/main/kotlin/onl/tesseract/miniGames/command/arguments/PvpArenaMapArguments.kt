package onl.tesseract.miniGames.command.arguments

import onl.tesseract.commandBuilder.CommandArgument
import onl.tesseract.commandBuilder.CommandArgumentBuilderSteps
import onl.tesseract.miniGames.MiniGamesPlugin
import onl.tesseract.miniGames.PVP_ARENA
import onl.tesseract.miniGames.minigames.pvparena.PvpArenaMap

class PvpArenaMapArguments(name: String) : CommandArgument<PvpArenaMap>(name) {
    override fun define(builder: CommandArgumentBuilderSteps.Parser<PvpArenaMap>) {
        builder.parser { input, _ ->
            MiniGamesPlugin.instance.miniGames[PVP_ARENA]!!.maps[input] as PvpArenaMap
        }
                .tabCompleter { input, env ->
                    MiniGamesPlugin.instance.miniGames[PVP_ARENA]!!.maps
                            .filter { it.key.startsWith(input) }.keys.toList()
                }
                .errorHandler(IllegalArgumentException::class.java, "Maps invalide")
    }
}