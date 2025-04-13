package onl.tesseract.miniGames.minigames.bedwars

import onl.tesseract.miniGames.BEDWARS_GAMES
import onl.tesseract.miniGames.MiniGamesPlugin
import onl.tesseract.miniGames.minigames.MiniGame
import onl.tesseract.miniGames.minigames.MiniGameMap
import org.bukkit.Location
import org.bukkit.configuration.ConfigurationSection

class Bedwars(val plugin: MiniGamesPlugin) : MiniGame(BEDWARS_GAMES){
    override fun loadMaps(configuration: ConfigurationSection){
        for(x in configuration.getKeys(false)) {
            maps[x] = BedwarsMap.of(configuration.getConfigurationSection(x)!!)
        }
    }

    override fun createMap(name: String, location: Location): MiniGameMap {
        return BedwarsMap(name, location)
    }

}