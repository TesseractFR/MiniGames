package onl.tesseract.miniGames.command.admin

import onl.tesseract.commandBuilder.annotation.Argument
import onl.tesseract.commandBuilder.annotation.Command
import onl.tesseract.lib.command.argument.IntegerCommandArgument
import onl.tesseract.miniGames.MiniGamesPlugin
import onl.tesseract.miniGames.command.arguments.BedwarsMapArguments
import onl.tesseract.miniGames.command.arguments.MaterialArguments
import onl.tesseract.miniGames.minigames.bedwars.BedwarsMap
import onl.tesseract.miniGames.minigames.bedwars.OreGenerator
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

@Command(
    name = "bedwars"

)
class BedwarsCommand {
    @Command
    fun addGenerator(@Argument(value = "arena", clazz = BedwarsMapArguments::class) map: BedwarsMap,
                     @Argument(value = "material", clazz = MaterialArguments::class) material: Material,
                     @Argument(value = "freq", clazz = IntegerCommandArgument::class) freq : Integer,
                     sender: Player)
    {
        map.addGenerator(OreGenerator(ItemStack.of(material),sender.location,MiniGamesPlugin.instance,freq.toLong()))
    }

    @Command
    fun openBoutique(@Argument(value = "arena", clazz = BedwarsMapArguments::class) map: BedwarsMap,
                     sender: Player){
        map.openBoutique(sender)
    }
    @Command
    fun openUpgradeBoutique(@Argument(value = "arena", clazz = BedwarsMapArguments::class) map: BedwarsMap,
                     sender: Player){
        map.openUpgradeBoutique(sender)
    }
}