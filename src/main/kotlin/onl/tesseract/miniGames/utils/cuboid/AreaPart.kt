package onl.tesseract.miniGames.utils.cuboid

import org.bukkit.Location
import org.bukkit.World
import org.bukkit.configuration.ConfigurationSection
import org.bukkit.util.Vector

interface AreaPart
{
    operator fun contains(location: Location): Boolean

    /**
     * Move the area by the given vector.
     *
     * @return A moved copy of the area
     */
    fun at(vector: Vector): AreaPart

    /**
     * Change the world of the area
     *
     * @return A copy of the area
     */
    fun at(world: World): AreaPart

    /**
     * Return a set of all blocks' location contained in the area
     */
    fun all(): Set<Location>

    fun center(): Location

    /**
     * Serializes the area part in a ConfigurationSection. Implementations should at minimum store a `type` field used for deserialization.
     */
    fun serialize(): ConfigurationSection

    fun getWorld(): World


}