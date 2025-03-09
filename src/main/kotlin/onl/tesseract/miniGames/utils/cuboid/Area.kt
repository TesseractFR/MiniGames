package onl.tesseract.miniGames.utils.cuboid

import onl.tesseract.lib.exception.ConfigurationException
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.World
import org.bukkit.configuration.ConfigurationSection
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.util.Vector
import java.util.stream.Collectors

/**
 * Defines an area in a world. An area is composed of multiple AreaParts to represent a complex geometric shape
 */
class Area(private val world: World, private val parts: Collection<AreaPart>) : AreaPart
{
    constructor(world: World) : this(world, ArrayList<AreaPart>())

    override fun contains(location: Location): Boolean
    {
        return parts.any { cuboid: AreaPart -> cuboid.contains(location) }
    }

    override fun at(vector: Vector): Area
    {
        return Area(world, parts.map { it.at(vector) })
    }

    override fun at(world: World): Area
    {
        return Area(world, parts.map { it.at(world) })
    }

    override fun getWorld(): World = world

    override fun all(): Set<Location>
    {
        return parts.stream()
            .map { it.all() }
            .flatMap { it.stream() }
            .collect(Collectors.toSet())
    }

    override fun center(): Location
    {
        var x = 0
        var y = 0
        var z = 0
        parts.map { it.center() }
            .forEach { x += it.blockX; y += it.blockY; z += it.blockZ }
        val partsCount = parts.size
        return Location(world, x.toDouble() / partsCount, y.toDouble() / partsCount, z.toDouble() + partsCount)
    }

    override fun serialize(): ConfigurationSection
    {
        val yaml: ConfigurationSection = YamlConfiguration()
        yaml["world"] = world.name
        val i = 0
        for (part in parts)
            yaml["parts.$i"] = part.serialize()
        return yaml
    }

    companion object
    {
        @JvmStatic
        @Throws(ConfigurationException::class)
        fun deserialize(section: ConfigurationSection): Area
        {
            val worldName = section.getString("world") ?: throw ConfigurationException("Missing 'world' field")
            val world = Bukkit.getWorld(worldName) ?: throw ConfigurationException("Unknown world '$worldName'")

            val pairs: MutableCollection<AreaPart> = ArrayList()
            val partsSection = section.getConfigurationSection("parts")
                ?: return Area(world, pairs)
            for (key in partsSection.getKeys(false))
            {
                val partSection = partsSection.getConfigurationSection(key) ?: continue
                val type = partSection.getString("type")

                if (type.equals("cuboid"))
                    pairs.add(Cuboid.deserialize(partSection))
                else if (type.equals("sphere"))
                    pairs.add(SphereArea.deserialize(partSection))
                else
                    throw ConfigurationException("Unsupported type for AreaPart: '$type'")
            }
            return Area(world, pairs)
        }
    }
}