package onl.tesseract.miniGames


import onl.tesseract.miniGames.command.MinigamesAdminCommand
import onl.tesseract.miniGames.command.MinigamesCommand
import onl.tesseract.miniGames.minigames.MiniGame
import onl.tesseract.miniGames.minigames.tntrun.TntRun
import onl.tesseract.miniGames.utils.MINIGAMES_FOLDER_NAME
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityChangeBlockEvent
import org.bukkit.plugin.java.JavaPlugin
import java.io.File
import java.util.*


class MiniGamesPlugin : JavaPlugin(), Listener {

    val miniGames = mutableMapOf<String, MiniGame>()
    private val players = mutableMapOf<UUID,MiniGamesPlayer>()

    override fun onEnable() {
        instance = this
        loadMiniGames()
        loadConfig()
        server.pluginManager.registerEvents(this,this)
        this.getCommand("minigames")?.setExecutor(MinigamesCommand())
        this.getCommand("minigamesadmin")?.setExecutor(MinigamesAdminCommand())

    }

    override fun onDisable() {
        saveMiniGames()
        saveConfig()
    }

    private fun loadMiniGames() {
        miniGames.clear()
        miniGames[TNT_RUN_GAMES] = TntRun(this)
    }
    private fun saveMiniGames() {
        miniGames.forEach {
            it.value.saveConfig()
        }
    }

    private fun loadConfig(){
        val file = File("$MINIGAMES_FOLDER_NAME/config.yml")
        val yaml = YamlConfiguration.loadConfiguration(file)
    }

    override fun saveConfig(){
        val file = File("$MINIGAMES_FOLDER_NAME/config.yml")
        val yaml = YamlConfiguration.loadConfiguration(file)
        saveMiniGames()
        yaml.save(file)
    }

    @EventHandler
    fun onBlockGravity(event : EntityChangeBlockEvent){
        if(event.block.type.hasGravity())
            event.isCancelled = true
    }

    companion object {
        lateinit var instance: MiniGamesPlugin
    }
}
