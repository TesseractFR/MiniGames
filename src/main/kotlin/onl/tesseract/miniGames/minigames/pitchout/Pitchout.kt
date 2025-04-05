package onl.tesseract.miniGames.minigames.pitchout

import onl.tesseract.miniGames.MiniGamesPlugin
import onl.tesseract.miniGames.PITCHOUT
import onl.tesseract.miniGames.minigames.MiniGame
import onl.tesseract.miniGames.minigames.MiniGameMap
import org.bukkit.Location
import org.bukkit.configuration.ConfigurationSection

class Pitchout(val plugin: MiniGamesPlugin) : MiniGame(PITCHOUT){
    override fun loadMaps(configuration: ConfigurationSection){
        for(x in configuration.getKeys(false)) {
            maps[x] = PitchoutMap.of(configuration.getConfigurationSection(x)!!)
        }
    }

    override fun createMap(name: String, location: Location): MiniGameMap {
        return PitchoutMap(name, location)
    }
}