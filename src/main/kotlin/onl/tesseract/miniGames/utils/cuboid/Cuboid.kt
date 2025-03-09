package onl.tesseract.miniGames.utils.cuboid


import com.fastasyncworldedit.core.Fawe
import com.fastasyncworldedit.core.FaweAPI
import com.sk89q.worldedit.WorldEdit
import onl.tesseract.lib.exception.ConfigurationException
import onl.tesseract.miniGames.MiniGamesPlugin
import org.bukkit.Location
import org.bukkit.World
import org.bukkit.configuration.ConfigurationSection
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.util.Vector
import java.util.*
import kotlin.math.max
import kotlin.math.min

class Cuboid(a: Location, b: Location) : AreaPart {
    var low: Location
    var high: Location

    init {
        require(a.world.uid == b.world.uid) { "Must be on same world" }

        val x1 = min(a.blockX.toDouble(), b.blockX.toDouble()).toInt()
        val y1 = min(a.blockY.toDouble(), b.blockY.toDouble()).toInt()
        val z1 = min(a.blockZ.toDouble(), b.blockZ.toDouble()).toInt()

        val x2 = max(a.blockX.toDouble(), b.blockX.toDouble()).toInt()
        val y2 = max(a.blockY.toDouble(), b.blockY.toDouble()).toInt()
        val z2 = max(a.blockZ.toDouble(), b.blockZ.toDouble()).toInt()

        low = Location(a.world, x1.toDouble(), y1.toDouble(), z1.toDouble())
        high = Location(a.world, x2.toDouble(), y2.toDouble(), z2.toDouble())
    }

    override fun contains(location: Location): Boolean {
        val world = low.world
        if (location.world == null || location.world.uid !== world.uid) return false
        if (location.blockX >= low.blockX && location.blockY >= low.blockY && location.blockZ >= low.blockZ) return location.blockX <= high.blockX && location.blockY <= high.blockY && location.blockZ <= high.blockZ
        return false
    }

    override fun at(vector: Vector): Cuboid {
        return Cuboid(
            low.clone()
                    .add(vector),
            high.clone()
                    .add(vector))
    }

    override fun at(world: World): Cuboid {
        val low = this.low.clone()
        val high = this.high.clone()
        low.world = world
        high.world = low.world
        return Cuboid(low, high)
    }

    override fun all(): Set<Location> {
        val res: MutableSet<Location> = HashSet()
        for (x in low.blockX..high.blockX) for (y in low.blockY..high.blockY) for (z in low.blockZ..high.blockZ) res.add(
            (Location(low.world, x.toDouble(), y.toDouble(), z.toDouble())))
        return res
    }

    override fun center(): Location {
        return Location(
            low.world,
            high.x + low.x / 2,
            high.y + low.y / 2,
            high.z + low.z / 2
        )
    }

    override fun serialize(): ConfigurationSection {
        val yaml: ConfigurationSection = YamlConfiguration()
        yaml["low"] = low
        yaml["high"] = high
        yaml["type"] = "cuboid"
        return yaml
    }

    override fun getWorld(): World {
        return high.world
    }

    fun resetCorner(x1: Double, y1: Double, z1: Double, x2: Double, y2: Double, z2: Double) {
        high = Location(high.world,max(x1,x2), max(y1,y2), max(z1,z2))
        low = Location(high.world, min(x1,x2), min(y1,y2), min(z1,z2))
    }


    companion object {
        @Throws(ConfigurationException::class)
        fun deserialize(section: ConfigurationSection): Cuboid {
            try {
                val low = Objects.requireNonNull(section.getLocation("low"))
                val high = Objects.requireNonNull(section.getLocation("high"))
                return Cuboid(low!!, high!!)
            } catch (e: NullPointerException) {
                throw ConfigurationException(e.message!!)
            }
        }
    }
}
