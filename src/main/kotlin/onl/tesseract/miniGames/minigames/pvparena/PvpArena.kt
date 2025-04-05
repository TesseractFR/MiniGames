package onl.tesseract.miniGames.minigames.pvparena

import onl.tesseract.miniGames.MiniGamesPlugin
import onl.tesseract.miniGames.PVP_ARENA
import onl.tesseract.miniGames.minigames.MiniGame
import onl.tesseract.miniGames.minigames.MiniGameMap
import org.bukkit.Location
import org.bukkit.configuration.ConfigurationSection

class PvpArena  (val plugin: MiniGamesPlugin) : MiniGame(PVP_ARENA){
    override fun loadMaps(configuration: ConfigurationSection){
        for(x in configuration.getKeys(false)) {
            maps[x] = PvpArenaMap.of(configuration.getConfigurationSection(x)!!)
        }
    }

    override fun createMap(name: String, location: Location): MiniGameMap {
        return PvpArenaMap(name, location)
    }

}