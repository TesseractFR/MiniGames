package onl.tesseract.miniGames.minigames.sumo

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import onl.tesseract.lib.util.ItemBuilder
import onl.tesseract.lib.util.append
import onl.tesseract.miniGames.SUMO_GAMES
import onl.tesseract.miniGames.minigames.ARENA
import onl.tesseract.miniGames.minigames.MiniGameMap
import onl.tesseract.miniGames.minigames.PLAYSPAWN
import onl.tesseract.miniGames.minigames.SPAWN
import onl.tesseract.miniGames.utils.cuboid.Cuboid
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.configuration.ConfigurationSection
import org.bukkit.enchantments.Enchantment.KNOCKBACK
import org.bukkit.entity.Player

class SumoMap(name: String, spawn : Location) : MiniGameMap(name, spawn, SUMO_GAMES){
    companion object {
        fun of(configurationSection: ConfigurationSection): MiniGameMap {
            val name = configurationSection.name
            val spawn = configurationSection.getLocation(SPAWN) ?: Bukkit.getWorlds()[0].spawnLocation
            val map = SumoMap(name, spawn)
            map.arena = configurationSection.getConfigurationSection(ARENA)
                    ?.let { Cuboid.deserialize(it) } ?: Cuboid(spawn, spawn)
            if(configurationSection.contains(PLAYSPAWN)) {
                for(x in configurationSection.getList(PLAYSPAWN)!!) {
                    map.playSpawn.add(x as Location)
                }
            }
            return map
        }
    }

    override fun playerLeaveArena(player: Player) {
        eliminatePlayer(player)
    }

    override fun getHeaderComponent(): Component {
        return Component.text("[", NamedTextColor.GOLD)
                .append(Component.text("Sumo", NamedTextColor.AQUA))
                .append(Component.text("] ", NamedTextColor.GOLD))
    }

    override fun canPVPDuringGame(): Boolean {
        return true
    }

    override fun setInventory(player: Player) {
        super.setInventory(player)
        giveStick(player)
    }

    private fun giveStick(player: Player) {
        val stick = ItemBuilder(Material.STICK)
                .name(Component.text("[", NamedTextColor.GOLD)
                        .append("Gros bâton", NamedTextColor.YELLOW)
                        .append("]", NamedTextColor.GOLD))
                .build()
        stick.addUnsafeEnchantment(KNOCKBACK, 2)
        player.inventory.addItem(stick)
    }

}