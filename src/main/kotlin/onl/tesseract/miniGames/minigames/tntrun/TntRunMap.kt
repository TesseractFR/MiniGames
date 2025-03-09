package onl.tesseract.miniGames.minigames.tntrun

import onl.tesseract.miniGames.minigames.MiniGameMap
import onl.tesseract.miniGames.utils.cuboid.Cuboid
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.configuration.ConfigurationSection


private const val SPAWN = "spawn"
private const val ARENA = "arena"

class TntRunMap(name: String, spawn : Location) : MiniGameMap(name, spawn) {

    override fun start() {
        TODO("Not yet implemented")
    }

    override fun stop() {
        TODO("Not yet implemented")
    }

    override fun save(createSection: ConfigurationSection) {
        createSection[SPAWN]=spawn
        createSection[ARENA]=arena.serialize()
    }

    companion object {
        fun of(configurationSection: ConfigurationSection?): MiniGameMap {
            val name = configurationSection?.getString("name")?:configurationSection!!.name
            val spawn = configurationSection?.getLocation(SPAWN)?:Bukkit.getWorlds()[0].spawnLocation
            val map = TntRunMap(name,spawn);
            map.arena = configurationSection?.getConfigurationSection(ARENA)
                    ?.let { Cuboid.deserialize(it) } ?:Cuboid(spawn,spawn)
            return map
        }
    }

}