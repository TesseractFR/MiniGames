package onl.tesseract.miniGames.minigames.pvparena

import onl.tesseract.miniGames.MiniGamesPlugin
import onl.tesseract.miniGames.PVP_ARENA
import onl.tesseract.miniGames.minigames.MiniGame
import org.bukkit.configuration.ConfigurationSection

class PvpArena  (val plugin: MiniGamesPlugin) : MiniGame(PVP_ARENA){
    override fun loadMaps(configuration: ConfigurationSection){
        for(x in configuration.getKeys(false)) {
            maps[x] = PvpArenaMap.of(configuration.getConfigurationSection(x)!!)
        }
    }

}