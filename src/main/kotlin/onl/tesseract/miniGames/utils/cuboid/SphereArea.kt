package onl.tesseract.miniGames.utils.cuboid

import onl.tesseract.lib.exception.ConfigurationException
import org.bukkit.Location
import org.bukkit.World
import org.bukkit.configuration.ConfigurationSection
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.util.Vector

class SphereArea(val center: Location, val radius: Int) : AreaPart
{
    override fun contains(location: Location): Boolean
    {
        return location.world.uid == center.world.uid && location.distance(center) <= radius
    }

    override fun at(vector: Vector): AreaPart
    {
        return SphereArea(center.add(vector), radius)
    }

    override fun at(world: World): AreaPart
    {
        val newCenter = center.clone()
        newCenter.world = world
        return SphereArea(newCenter, radius)
    }

    override fun all(): Set<Location>
    {
        // TODO Not yet implemented
        return emptySet()
    }

    override fun center(): Location
    {
        return center
    }

    override fun serialize(): ConfigurationSection
    {
        val config = YamlConfiguration()
        config.set("type", "sphere")
        config.set("center", center)
        config.set("radius", radius)
        return config
    }

    override fun getWorld(): World {
        return center.world
    }

    companion object
    {
        @JvmStatic
        fun deserialize(config: ConfigurationSection): SphereArea
        {
            val radius = config.getInt("radius")
            val center = config.getLocation("center") ?: throw ConfigurationException("Missing field 'center'")
            return SphereArea(center, radius)
        }
    }
}