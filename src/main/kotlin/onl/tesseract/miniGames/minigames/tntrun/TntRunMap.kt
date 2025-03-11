package onl.tesseract.miniGames.minigames.tntrun

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import onl.tesseract.miniGames.MiniGamesPlugin
import onl.tesseract.miniGames.minigames.ARENA
import onl.tesseract.miniGames.minigames.MiniGameMap
import onl.tesseract.miniGames.minigames.PLAYSPAWN
import onl.tesseract.miniGames.minigames.SPAWN
import onl.tesseract.miniGames.utils.cuboid.Cuboid
import onl.tesseract.miniGames.utils.enums.ArenaState
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.configuration.ConfigurationSection
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerMoveEvent
import org.bukkit.scheduler.BukkitRunnable


class TntRunMap(name: String, spawn : Location) : MiniGameMap(name, spawn) {

    override fun start() {
        super.start()
        MiniGamesPlugin.instance.server.pluginManager.registerEvents(this, MiniGamesPlugin.instance)

    }

    override fun stop() {
        super.stop()
    }

    override fun save(createSection: ConfigurationSection) {
        super.save(createSection)
    }

    override fun playerLeaveArena(player: Player) {
        eliminatePlayer(player)

    }

    override fun getHeaderComponent(): Component {
        return Component.text("[", NamedTextColor.GOLD)
                .append(Component.text("TntRun", NamedTextColor.AQUA))
                .append(Component.text("] ", NamedTextColor.GOLD))
    }

    companion object {
        fun of(configurationSection: ConfigurationSection): MiniGameMap {
            val name = configurationSection.name
            val spawn = configurationSection.getLocation(SPAWN) ?: Bukkit.getWorlds()[0].spawnLocation
            val map = TntRunMap(name, spawn)
            map.arena = configurationSection.getConfigurationSection(ARENA)
                    ?.let { Cuboid.deserialize(it) } ?: Cuboid(spawn, spawn)
            if(configurationSection.contains(PLAYSPAWN)) {
                for(x in configurationSection.getList(PLAYSPAWN)!!) {
                    map.playSpawn.add(x as Location)
                }
            }
            return map
        }
    }

    @EventHandler
    fun onPlayerMoveTnt(event: PlayerMoveEvent) {
        if (arenaState == ArenaState.INGAME) {
            if (event.player in players) {
                val l = event.player.location.clone()
                l.y-=1
                object : BukkitRunnable() {
                    override fun run() {
                        l.block.type = Material.AIR
                    }
                }.runTaskLater(MiniGamesPlugin.instance, 5)
            }
        }
    }

}