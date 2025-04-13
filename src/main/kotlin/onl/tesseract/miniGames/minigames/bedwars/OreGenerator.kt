package onl.tesseract.miniGames.minigames.bedwars

import onl.tesseract.miniGames.MiniGamesPlugin
import org.bukkit.Location
import org.bukkit.configuration.serialization.ConfigurationSerializable
import org.bukkit.inventory.ItemStack
import org.bukkit.plugin.Plugin
import org.bukkit.scheduler.BukkitRunnable




class OreGenerator(val item: ItemStack,val location: Location,val plugin: Plugin,val timer: Long) :
ConfigurationSerializable {
    constructor(map: Map<String?, Any?>?) : this(map!!["item"] as ItemStack, map["location"] as Location,MiniGamesPlugin.instance, (map["timer"] as Int).toLong())

    lateinit var task : BukkitRunnable

    fun start(){
        task = object : BukkitRunnable(){
            override fun run() {
                location.world.dropItem(location, item)
            }
        }
        task.runTaskTimer(plugin,timer,timer*20)
    }

    fun stop(){
        task.cancel()
    }


    override fun serialize(): MutableMap<String, Any> {
        val map: MutableMap<String, Any> = HashMap()
        map["item"] = item
        map["location"] = location
        map["timer"] = timer
        return map
    }

    fun deserialize(map: Map<String?, Any?>?): OreGenerator {
        return OreGenerator(map)
    }

}