package onl.tesseract.miniGames


import onl.tesseract.lib.TesseractLib
import onl.tesseract.miniGames.command.MinigamesAdminCommand
import onl.tesseract.miniGames.command.MinigamesCommand
import onl.tesseract.miniGames.minigames.MiniGame
import onl.tesseract.miniGames.minigames.pitchout.Pitchout
import onl.tesseract.miniGames.minigames.pvparena.PvpArena
import onl.tesseract.miniGames.minigames.quake.Quake
import onl.tesseract.miniGames.minigames.sumo.Sumo
import onl.tesseract.miniGames.minigames.tntrun.TntRun
import onl.tesseract.miniGames.utils.MINIGAMES_FOLDER_NAME
import onl.tesseract.miniGames.utils.enums.ArenaState
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.entity.EntityType
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityChangeBlockEvent
import org.bukkit.event.entity.EntityDamageEvent
import org.bukkit.plugin.java.JavaPlugin
import java.io.File


class MiniGamesPlugin : JavaPlugin(), Listener {

    val miniGames = mutableMapOf<String, MiniGame>()

    override fun onEnable() {
        instance = this
        TesseractLib.registerOnEnable(this)
        TesseractLib.loadInventoryConfigurations()
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
        miniGames[SUMO_GAMES] = Sumo(this)
        miniGames[PVP_ARENA] = PvpArena(this)
        miniGames[PITCHOUT] = Pitchout(this)
        miniGames[QUAKE] = Quake(this)
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



    @EventHandler
    fun onDamage(event : EntityDamageEvent) {
        if (event.entity.type != EntityType.PLAYER)
            return
        val player = event.entity as Player
        for(game in miniGames.values){
            for (map in game.maps.values){
                if(map.arenaState == ArenaState.INGAME)
                    return
                if(player in map.players){
                    event.isCancelled = true
                    return
                }
            }
        }
    }

    companion object {
        lateinit var instance: MiniGamesPlugin
    }
}
