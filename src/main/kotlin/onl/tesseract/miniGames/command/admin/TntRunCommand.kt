package onl.tesseract.miniGames.command.admin

import net.kyori.adventure.text.Component
import onl.tesseract.commandBuilder.annotation.Argument
import onl.tesseract.commandBuilder.annotation.Command
import onl.tesseract.lib.command.argument.IntegerCommandArgument
import onl.tesseract.lib.command.argument.StringArg
import onl.tesseract.miniGames.MiniGamesPlugin
import onl.tesseract.miniGames.TNT_RUN_GAMES
import onl.tesseract.miniGames.command.arguments.TntRunMapArguments
import onl.tesseract.miniGames.minigames.tntrun.TntRunMap
import org.bukkit.entity.Player

@Command(
    name = "tntgames"
)
class TntRunCommand {

    private val miniGame = MiniGamesPlugin.instance.miniGames[TNT_RUN_GAMES]!!
    private val maps = miniGame.maps

    @Command(playerOnly = true)
    fun setSpawn(@Argument(value = "arena", clazz = TntRunMapArguments::class) map: TntRunMap, sender: Player) {
        map.spawn = sender.location
    }

    @Command(playerOnly = true)
    fun listCommand(sender: Player) {
        val maps = this.maps.entries.toList()
                .joinToString { it.key + " " }
        sender.sendMessage(Component.text("Liste des maps : $maps"))
    }

    @Command
    fun createCommand(@Argument(value = "name", clazz = StringArg::class) name: String, sender: Player) {
        val maps = this.maps
        if (maps.containsKey(name)) {
            sender.sendMessage(Component.text("La map $name existe déjà"))
            return
        }
        maps[name] = TntRunMap(name, sender.location)
        miniGame.saveConfig()
        sender.sendMessage(Component.text("La map $name a été créé"))
    }

    @Command
    fun deleteCommand(@Argument(value = "arena", clazz = TntRunMapArguments::class) map: TntRunMap, sender: Player) {
        this.maps.remove(map.name)
        sender.sendMessage(Component.text("La map ${map.name} a été supprimée"))
    }

    @Command
    fun setArenaCommand(
        @Argument(value = "arena", clazz = TntRunMapArguments::class) map: TntRunMap,
        @Argument(value = "x1", clazz = IntegerCommandArgument::class) x1: Int,
        @Argument(value = "y1", clazz = IntegerCommandArgument::class) y1: Int,
        @Argument(value = "z1", clazz = IntegerCommandArgument::class) z1: Int,
        @Argument(value = "x2", clazz = IntegerCommandArgument::class) x2: Int,
        @Argument(value = "y2", clazz = IntegerCommandArgument::class) y2: Int,
        @Argument(value = "z2", clazz = IntegerCommandArgument::class) z2: Int,
        sender: Player,
    ) {
        map.setArena(x1.toDouble(), y1.toDouble(), z1.toDouble(), x2.toDouble(), y2.toDouble(), z2.toDouble())
        sender.sendMessage("Arene de jeu set")
    }


    @Command
    fun resetArenaCommand(
        @Argument(value = "arena", clazz = TntRunMapArguments::class) map: TntRunMap,
        sender: Player,
    ) {
        map.resetArena()
    }

    @Command
    fun startCommand(@Argument(value = "arena", clazz = TntRunMapArguments::class) map: TntRunMap) {
        map.start()
    }

    @Command
    fun addPlaySpawnCommand(
        @Argument(value = "arena", clazz = TntRunMapArguments::class) map: TntRunMap,
        sender: Player,
    ) {
        map.playSpawn.add(sender.location)
    }


}