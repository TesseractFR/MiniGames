package onl.tesseract.miniGames.minigames.sumo

import onl.tesseract.miniGames.MiniGamesPlugin
import onl.tesseract.miniGames.SUMO_GAMES
import onl.tesseract.miniGames.minigames.MiniGame
import onl.tesseract.miniGames.minigames.MiniGameMap
import onl.tesseract.miniGames.minigames.tntrun.TntRunMap
import org.bukkit.Location
import org.bukkit.configuration.ConfigurationSection

class Sumo (val plugin: MiniGamesPlugin) : MiniGame(SUMO_GAMES){
    override fun loadMaps(configuration: ConfigurationSection){
        for(x in configuration.getKeys(false)) {
            maps[x] = SumoMap.of(configuration.getConfigurationSection(x)!!)
        }
    }

    override fun createMap(name: String, location: Location): MiniGameMap {
        return TntRunMap(name, location)
    }


}