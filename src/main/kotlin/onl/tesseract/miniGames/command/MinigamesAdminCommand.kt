package onl.tesseract.miniGames.command

import net.kyori.adventure.text.Component
import onl.tesseract.commandBuilder.CommandContext
import onl.tesseract.commandBuilder.annotation.Argument
import onl.tesseract.commandBuilder.annotation.Command
import onl.tesseract.lib.command.argument.IntegerCommandArgument
import onl.tesseract.lib.command.argument.StringArg
import onl.tesseract.miniGames.MiniGamesPlugin
import onl.tesseract.miniGames.command.admin.BedwarsCommand
import onl.tesseract.miniGames.command.arguments.MiniGameArgument
import onl.tesseract.miniGames.command.arguments.MiniGamesMapArgument
import onl.tesseract.miniGames.minigames.MiniGame
import onl.tesseract.miniGames.minigames.MiniGameMap
import org.bukkit.entity.Player

@Command(
    name = "minigamesadmin",
    subCommands = [BedwarsCommand::class],
)
class MinigamesAdminCommand :CommandContext() {
    @Command
    fun resetAllCommand(){
        for(game in MiniGamesPlugin.instance.miniGames.values){
            for (map in game.maps.values){
                map.resetArena()
            }
        }
    }


    @Command(playerOnly = true)
    fun setSpawn(@Argument(value = "minigame", clazz = MiniGameArgument::class) minigame : MiniGame,
                 @Argument(value = "arena", clazz = MiniGamesMapArgument::class) map: MiniGameMap,
                 sender: Player) {
        map.spawn = sender.location
    }

    @Command(playerOnly = true)
    fun listCommand(@Argument(value = "minigame", clazz = MiniGameArgument::class) minigame : MiniGame,
                    sender: Player) {
        val maps = minigame.maps.entries.toList()
                .joinToString { it.key + " " }
        sender.sendMessage(Component.text("Liste des maps : $maps"))
    }

    @Command
    fun createCommand(@Argument(value = "minigame", clazz = MiniGameArgument::class) minigame : MiniGame,
                      @Argument(value = "name", clazz = StringArg::class) name: String, sender: Player) {
        val maps = minigame.maps
        if (maps.containsKey(name)) {
            sender.sendMessage(Component.text("La map $name existe déjà"))
            return
        }
        maps[name] = minigame.createMap(name, sender.location)
        minigame.saveConfig()
        sender.sendMessage(Component.text("La map $name a été créé"))
    }

    @Command
    fun deleteCommand(@Argument(value = "minigame", clazz = MiniGameArgument::class) minigame : MiniGame,
                      @Argument(value = "arena", clazz = MiniGamesMapArgument::class) map: MiniGameMap,
                      sender: Player) {
        minigame.maps.remove(map.name)
        sender.sendMessage(Component.text("La map ${map.name} a été supprimée"))
    }

    @Command
    fun setArenaCommand(
        @Argument(value = "minigame", clazz = MiniGameArgument::class) minigame : MiniGame,
        @Argument(value = "arena", clazz = MiniGamesMapArgument::class) map: MiniGameMap,
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
        @Argument(value = "minigame", clazz = MiniGameArgument::class) minigame : MiniGame,
        @Argument(value = "arena", clazz = MiniGamesMapArgument::class) map: MiniGameMap,
        sender: Player,
    ) {
        map.resetArena()
    }

    @Command
    fun startCommand(
        @Argument(value = "minigame", clazz = MiniGameArgument::class) minigame : MiniGame,
        @Argument(value = "arena", clazz = MiniGamesMapArgument::class) map: MiniGameMap) {
        map.start()
    }

    @Command
    fun addPlaySpawnCommand(
        @Argument(value = "minigame", clazz = MiniGameArgument::class) minigame : MiniGame,
        @Argument(value = "arena", clazz = MiniGamesMapArgument::class) map: MiniGameMap,
        sender: Player,
    ) {
        map.playSpawn.add(sender.location)
    }
}