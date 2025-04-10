package onl.tesseract.miniGames.minigames.quake

import onl.tesseract.miniGames.MiniGamesPlugin
import onl.tesseract.miniGames.QUAKE
import onl.tesseract.miniGames.minigames.MiniGame
import onl.tesseract.miniGames.minigames.MiniGameMap
import onl.tesseract.miniGames.minigames.sumo.SumoMap
import org.bukkit.Location
import org.bukkit.configuration.ConfigurationSection

class Quake(val plugin: MiniGamesPlugin) : MiniGame(QUAKE) {
    override fun loadMaps(configuration: ConfigurationSection){
        for(x in configuration.getKeys(false)) {
            maps[x] = QuakeMap.of(configuration.getConfigurationSection(x)!!)
        }
    }


    override fun createMap(name: String, location: Location): MiniGameMap {
        return QuakeMap(name, location)
    }

    override fun initInventory() {
        super.initInventory()

    }

}