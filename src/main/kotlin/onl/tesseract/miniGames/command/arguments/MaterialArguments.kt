package onl.tesseract.miniGames.command.arguments

import onl.tesseract.commandBuilder.CommandArgument
import onl.tesseract.commandBuilder.CommandArgumentBuilderSteps
import org.bukkit.Material

class MaterialArguments(name: String) : CommandArgument<Material>(name) {
    override fun define(builder: CommandArgumentBuilderSteps.Parser<Material>) {
        builder.parser { input, _ ->
            Material.valueOf(input)
        }
                .tabCompleter { input, _ ->
                    Material.values().map { it.toString() }.filter { it.startsWith(input)}.toList()
                }
                .errorHandler(IllegalArgumentException::class.java, "Maps invalide")
    }
}