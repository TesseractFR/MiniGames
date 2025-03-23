package onl.tesseract.miniGames.minigames.tntrun

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import onl.tesseract.miniGames.MiniGamesPlugin
import onl.tesseract.miniGames.TNT_RUN_GAMES
import onl.tesseract.miniGames.minigames.ARENA
import onl.tesseract.miniGames.minigames.MiniGameMap
import onl.tesseract.miniGames.minigames.PLAYSPAWN
import onl.tesseract.miniGames.minigames.SPAWN
import onl.tesseract.miniGames.utils.cuboid.Cuboid
import onl.tesseract.miniGames.utils.enums.ArenaState
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.block.Block
import org.bukkit.configuration.ConfigurationSection
import org.bukkit.entity.Player
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType
import org.bukkit.scheduler.BukkitRunnable


class TntRunMap(name: String, spawn : Location) : MiniGameMap(name, spawn, TNT_RUN_GAMES) {

    private lateinit var blockRemoveTask : BukkitRunnable

    private fun getBlockRemoveTask(): BukkitRunnable {
        return object : BukkitRunnable() {
            val toRemove = mutableListOf<Block>()
            override fun run() {
                if(this@TntRunMap.arenaState != ArenaState.INGAME){
                    return
                }
                for(block in toRemove){
                    if(block.location in this@TntRunMap.arena)
                        block.type = Material.AIR
                }
                toRemove.clear()
                for(player in this@TntRunMap.players){
                    toRemove.add(getBlockUnderPlayer(player.location))
                }
            }
            private fun getBlockUnderPlayer(location: Location): Block {
                val b1: Block = Location(location.world, location.x+0.3, location.y-1, location.z-0.3).block

                if (b1.type !== Material.AIR) {
                    return b1
                }

                val b2: Block = Location(location.world, location.x-0.3, location.y-1, location.z+0.3).block

                if (b2.type !== Material.AIR) {
                    return b2
                }

                val b3: Block = Location(location.world, location.x+0.3, location.y-1, location.z+0.3).block

                if (b3.type !== Material.AIR) {
                    return b3
                }

                return Location(location.world, location.x-0.3, location.y-1, location.z-0.3).block
            }
        }
    }


    override fun start() {
        super.start()
        MiniGamesPlugin.instance.server.pluginManager.registerEvents(this, MiniGamesPlugin.instance)
        blockRemoveTask = getBlockRemoveTask()
        blockRemoveTask.runTaskTimer(MiniGamesPlugin.instance,0,3)
        for(p in players){
            p.isCollidable = false
            p.addPotionEffect(PotionEffect(PotionEffectType.SATURATION,PotionEffect.INFINITE_DURATION,0,false,false))
        }

    }

    override fun eliminatePlayer(player: Player) {
        super.eliminatePlayer(player)
    }

    override fun stop() {
        blockRemoveTask.cancel()
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

    override fun canPVPDuringGame(): Boolean {
        return false
    }

    companion object {
        fun of(configurationSection: ConfigurationSection): MiniGameMap {
            val name = configurationSection.name
            val spawn = configurationSection.getLocation(SPAWN) ?: Bukkit.getWorlds()[0].spawnLocation
            val map = TntRunMap(name, spawn)
            (configurationSection.getConfigurationSection(ARENA)
                    ?.let { Cuboid.deserialize(it) } ?: Cuboid(spawn, spawn)).also { map.arena = it }
            if(configurationSection.contains(PLAYSPAWN)) {
                for(x in configurationSection.getList(PLAYSPAWN)!!) {
                    map.playSpawn.add(x as Location)
                }
            }
            return map
        }
    }



}