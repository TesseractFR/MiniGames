package onl.tesseract.miniGames.minigames.tntrun


import onl.tesseract.miniGames.MiniGamesPlugin
import onl.tesseract.miniGames.minigames.MiniGame
import org.bukkit.configuration.ConfigurationSection

class TntRun(val plugin: MiniGamesPlugin) : MiniGame("TntRun") {



    override fun loadMaps(configuration: ConfigurationSection) {
        for(x in configuration.getKeys(false)) {
            maps[x] = TntRunMap.of(configuration.getConfigurationSection(x))
        }
    }

    override fun saveMaps(createSection: ConfigurationSection) {
        for(map in maps){
            map.value.save(createSection.createSection(map.key))
        }
    }


}
