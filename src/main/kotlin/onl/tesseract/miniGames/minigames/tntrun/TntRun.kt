package onl.tesseract.miniGames.minigames.tntrun


import onl.tesseract.miniGames.MiniGamesPlugin
import onl.tesseract.miniGames.TNT_RUN_GAMES
import onl.tesseract.miniGames.minigames.MiniGame
import onl.tesseract.miniGames.minigames.MiniGameMap
import org.bukkit.Location
import org.bukkit.configuration.ConfigurationSection

class TntRun(val plugin: MiniGamesPlugin) : MiniGame(TNT_RUN_GAMES){
    override fun loadMaps(configuration: ConfigurationSection){
        for(x in configuration.getKeys(false)) {
            maps[x] = TntRunMap.of(configuration.getConfigurationSection(x)!!)
        }
    }

    override fun createMap(name: String, location: Location): MiniGameMap {
        return TntRunMap(name, location)
    }


}
