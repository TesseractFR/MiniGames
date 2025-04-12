package onl.tesseract.miniGames.minigames.pvparena

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import onl.tesseract.lib.menu.ItemBuilder
import onl.tesseract.miniGames.PVP_ARENA
import onl.tesseract.miniGames.minigames.ARENA
import onl.tesseract.miniGames.minigames.MiniGameMap
import onl.tesseract.miniGames.minigames.PLAYSPAWN
import onl.tesseract.miniGames.minigames.SPAWN
import onl.tesseract.miniGames.utils.cuboid.Cuboid
import onl.tesseract.miniGames.utils.enums.ArenaState
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.configuration.ConfigurationSection
import org.bukkit.enchantments.Enchantment
import org.bukkit.entity.Entity
import org.bukkit.entity.Player
import org.bukkit.entity.TNTPrimed
import org.bukkit.event.EventHandler
import org.bukkit.event.entity.EntityExplodeEvent
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.inventory.ItemStack
import java.time.Instant

class PvpArenaMap(name: String, spawn: Location) : MiniGameMap(name, spawn, PVP_ARENA) {
    private val TNT_TIMER = 1L
    private val tntCdMap = mutableMapOf<Player, Instant>()

    init {
        this.pvpEnabled = true
        this.lifeSystem = true
        this.maxLife = 3;
    }
    override fun playerLeaveArena(player: Player) {
        player.teleport(getRandomSpawn())
    }

    override fun playerKilled(player: Player, killer: Player?) {
        super.playerKilled(player, killer)
        if(player in players)
            player.teleport(getRandomSpawn())
    }

    override fun getHeaderComponent(): Component {
        return Component.text("[", NamedTextColor.GOLD)
                .append(Component.text("PvP", NamedTextColor.AQUA))
                .append(Component.text("] ", NamedTextColor.GOLD))
    }


    companion object {
        fun of(configurationSection: ConfigurationSection): PvpArenaMap {
            val name = configurationSection.name
            val spawn = configurationSection.getLocation(SPAWN) ?: Bukkit.getWorlds()[0].spawnLocation
            val map = PvpArenaMap(name, spawn)
            map.arena = configurationSection.getConfigurationSection(ARENA)
                    ?.let { Cuboid.deserialize(it) } ?: Cuboid(spawn, spawn)
            if (configurationSection.contains(PLAYSPAWN)) {
                for (x in configurationSection.getList(PLAYSPAWN)!!) {
                    map.playSpawn.add(x as Location)
                }
            }
            return map
        }
    }

    override fun setInventory(player: Player) {
        super.setInventory(player)
        val inventory = player.inventory
        inventory.setItemInOffHand(getShield())
        inventory.helmet = getHelmet()
        inventory.chestplate = getChestplate()
        inventory.leggings = getLegs()
        inventory.boots = getBoots()
        inventory.setItem(0, getSword())
        inventory.setItem(1, getBow())
        inventory.setItem(2, getArrow())
        inventory.setItem(3, getWindCharges())
        inventory.setItem(4, getGoldenApples())
        inventory.setItem(5, getSteaks())
        inventory.setItem(6, getTnt())
    }

    private fun getWindCharges(): ItemStack {
        return ItemBuilder(Material.WIND_CHARGE).amount(16)
                .build()
    }

    private fun getGoldenApples(): ItemStack {
        return ItemBuilder(Material.GOLDEN_APPLE).amount(8)
                .build()
    }

    private fun getSteaks(): ItemStack {
        return ItemBuilder(Material.COOKED_BEEF).amount(32)
                .build()
    }

    private fun getTnt(): ItemStack {
        return ItemBuilder(Material.TNT).amount(3)
                .build()
    }

    private fun getHelmet(): ItemStack {
        return ItemBuilder(Material.IRON_HELMET)
                .addEnchantment(Enchantment.UNBREAKING, 3)
                .build()
    }

    private fun getChestplate(): ItemStack {
        return ItemBuilder(Material.IRON_CHESTPLATE)
                .addEnchantment(Enchantment.UNBREAKING, 3)
                .build()
    }

    private fun getLegs(): ItemStack {
        return ItemBuilder(Material.IRON_LEGGINGS)
                .addEnchantment(Enchantment.UNBREAKING, 3)
                .build()
    }

    private fun getBoots(): ItemStack {
        return ItemBuilder(Material.IRON_BOOTS)
                .addEnchantment(Enchantment.UNBREAKING, 3)
                .build()
    }

    private fun getSword(): ItemStack {
        return ItemBuilder(Material.DIAMOND_SWORD)
                .addEnchantment(Enchantment.SHARPNESS, 2)
                .build()
    }

    private fun getBow(): ItemStack {
        return ItemBuilder(Material.BOW)
                .addEnchantment(Enchantment.POWER, 1)
                .build()
    }

    private fun getArrow(): ItemStack {
        return ItemBuilder(Material.ARROW)
                .amount(32)
                .build()
    }

    private fun getShield(): ItemStack {
        return ItemBuilder(Material.SHIELD).build()
    }

    override fun getRankingDetail(player: Player): Component {
        return Component.text(" (${playerKills[player]?:0} kills)", NamedTextColor.GRAY)
    }

    @EventHandler
    fun onPlayerTNT(event: PlayerInteractEvent) {
        val player = event.player
        if(player !in players || arenaState !=ArenaState.INGAME) {
            return
        }
        val item = event.item
        if(!event.hasBlock()){
            return
        }
        if(item == null || item.type != Material.TNT ) {
            return
        }
        if(tntCdMap.containsKey(player) && tntCdMap[player]!!.plusSeconds(TNT_TIMER).isAfter(Instant.now())){
            return
        }
        val tnt = player.world.spawn(event.clickedBlock!!.location.add(0.0, 1.0, 0.0),TNTPrimed::class.java) as TNTPrimed
        tnt.fuseTicks = 15
        tnt.source = player
        item.amount -= 1
        tntCdMap[player] = Instant.now()
    }

    @EventHandler
    fun onTNTExplode(event: EntityExplodeEvent) {
        event.blockList().clear()
    }
}