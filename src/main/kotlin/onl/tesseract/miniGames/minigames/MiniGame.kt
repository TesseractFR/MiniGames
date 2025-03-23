package onl.tesseract.miniGames.minigames

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer
import onl.tesseract.lib.inventory.InventoryInstanceConfigurationBuilder
import onl.tesseract.lib.inventory.InventoryInstanceManager
import onl.tesseract.miniGames.utils.MINIGAMES_GAMES_FOLDER_NAME
import org.bukkit.GameMode
import org.bukkit.configuration.ConfigurationSection
import org.bukkit.configuration.file.YamlConfiguration
import java.io.File


private val defaultExplicationComponent = Component.text(" ---------", NamedTextColor.GOLD)
        .appendNewline()
        .append(Component.text(" MINIGAME ", NamedTextColor.YELLOW))
        .appendNewline()
        .append(Component.text(" ----------", NamedTextColor.GOLD))

abstract class MiniGame(val name: String) {

    private val pathFile = "$MINIGAMES_GAMES_FOLDER_NAME/$name.yaml"
    protected val TUTO_MESSAGE_KEY = "Tutorial"
    protected val MAPS_KEY = "Maps"
    val maps = mutableMapOf<String,MiniGameMap>()

    lateinit var explicationComponent : Component;

    init {
        loadConfig()
        initInventory()
    }

    open fun initInventory(){
        InventoryInstanceManager.addConfig(
            InventoryInstanceConfigurationBuilder()
                    .setName(name)
                    .setRestrictInvocables(true)
                    .setWorld(null)
                    .setGameMode(GameMode.ADVENTURE)
                    .build())
    }

    protected fun loadConfig() : YamlConfiguration {
        val yamlConfiguration = YamlConfiguration.loadConfiguration(File(pathFile))
        explicationComponent = yamlConfiguration.getComponent(TUTO_MESSAGE_KEY, GsonComponentSerializer.gson())?:defaultExplicationComponent
        yamlConfiguration.getConfigurationSection(MAPS_KEY)?.let { loadMaps(it) }
        return yamlConfiguration
    }

    fun saveConfig(){
        File(pathFile).let {
            val yamlConfiguration = YamlConfiguration.loadConfiguration(it)
            yamlConfiguration.setComponent(TUTO_MESSAGE_KEY,GsonComponentSerializer.gson(),explicationComponent )
            saveMaps(yamlConfiguration.createSection(MAPS_KEY))
            yamlConfiguration.save(it)

        }

    }

    abstract fun loadMaps(configuration: ConfigurationSection)
    protected  fun saveMaps(createSection: ConfigurationSection){
        for(map in maps){
            map.value.save(createSection.createSection(map.key))
        }
    }
}