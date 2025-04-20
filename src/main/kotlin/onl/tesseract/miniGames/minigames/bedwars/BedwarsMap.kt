package onl.tesseract.miniGames.minigames.bedwars

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import onl.tesseract.lib.menu.ItemBuilder
import onl.tesseract.lib.util.append
import onl.tesseract.miniGames.BEDWARS_GAMES
import onl.tesseract.miniGames.minigames.ARENA
import onl.tesseract.miniGames.minigames.MiniGameMap
import onl.tesseract.miniGames.minigames.PLAYSPAWN
import onl.tesseract.miniGames.minigames.SPAWN
import onl.tesseract.miniGames.minigames.bedwars.menu.BedwarsShopMenu
import onl.tesseract.miniGames.minigames.bedwars.menu.BedwarsUpgradeMenu
import onl.tesseract.miniGames.utils.cuboid.Cuboid
import org.bukkit.*
import org.bukkit.block.Block
import org.bukkit.configuration.ConfigurationSection
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.block.BlockBreakEvent


class BedwarsMap(name: String, spawn: Location) : MiniGameMap(name, spawn, BEDWARS_GAMES) {
    val playerRespawn = mutableMapOf<Player, Location>()
    val playerColors = mutableMapOf<Player, String>()
    val playerEquipement = mutableMapOf<Player,BedwarsPlayerEquipement>()
    private val whitelistedBlocks = mutableSetOf(Material.END_STONE, Material.OAK_PLANKS,Material.TERRACOTTA,Material.OBSIDIAN)
    private val generators = mutableListOf<OreGenerator>()
    override fun playerLeaveArena(player: Player) {
        return
    }

    init{
        pvpEnabled = true
    }

    override fun start() {
        super.start()
        for(player in players) {
            val color = materialToColor(player.location.block.type)
            playerColors[player] = color
            playerRespawn[player] = player.location
            whitelistedBlocks.add(Material.valueOf(color+"_WOOL"))
            playerEquipement[player] = BedwarsPlayerEquipement(color)
            setInventory(player)
        }
        for (generator in generators) {
            generator.start()
        }
    }

    override fun resetArena() {
        super.resetArena()
        playerRespawn.clear()
        playerColors.clear()
        playerEquipement.clear()
        whitelistedBlocks.clear()
        whitelistedBlocks.addAll(mutableSetOf(Material.END_STONE, Material.OAK_PLANKS,Material.TERRACOTTA))
    }

    override fun stop() {
        super.stop()
        for (generator in generators) {
            generator.stop()
        }
    }


    override fun getHeaderComponent(): Component {
        return Component.text("[", NamedTextColor.GOLD)
                .append(Component.text("BedWars", NamedTextColor.AQUA))
                .append(Component.text("] ", NamedTextColor.GOLD))
    }

    override fun playerKilled(player: Player, killer: Player?) {
            player.health = player.maxHealth
            playerRespawn[player]?.let {
                displayPlayerKilled(player,killer)
                player.teleport(it)
            }?:eliminatePlayer(player)

    }


    @EventHandler
    fun onPlayerBreak(event:BlockBreakEvent){
        val player = event.player
        if(player !in players)return
        val block = event.block
        if(block.type in whitelistedBlocks){
            return
        }
        if(block.type.toString().contains("BED")){
            if (playerBreakBed(player, block)){
                return
            }
        }

        event.isCancelled = true
    }

    private fun playerBreakBed(player: Player, block: Block): Boolean {
        val mat = block.type
        val color = materialToColor(mat)
        if(color == playerColors[player]){
            return false
        }
        playerColors.forEach{
            if(it.value == color){
                playerRespawn.remove(it.key)
                broadcast(getHeaderComponent()
                        .append(" Le lit de ",NamedTextColor.YELLOW)
                        .append(getDisplayPlayerComponent(it.key)
                                .append(" a été cassé.",NamedTextColor.YELLOW))
                )
            }
        }
        return true
    }

    private fun materialToColor(material: Material):String{
        return material.toString().split("_")[0]
    }

    fun addGenerator(generator: OreGenerator){
        generators.add(generator)
    }

    companion object {
        fun of(configurationSection: ConfigurationSection): MiniGameMap {
            val name = configurationSection.name
            val spawn = configurationSection.getLocation(SPAWN) ?: Bukkit.getWorlds()[0].spawnLocation
            val map = BedwarsMap(name, spawn)
            map.arena = configurationSection.getConfigurationSection(ARENA)
                    ?.let { Cuboid.deserialize(it) } ?: Cuboid(spawn, spawn)
            if(configurationSection.contains(PLAYSPAWN)) {
                for(x in configurationSection.getList(PLAYSPAWN)!!) {
                    map.playSpawn.add(x as Location)
                }
            }
            if(configurationSection.contains("generator")){
                for(x in configurationSection.getList("generator")!!) {
                    map.addGenerator(x as OreGenerator)
                }
            }
            return map
        }
    }

    override fun setInventory(player: Player) {
        super.setInventory(player)
        player.gameMode = GameMode.SURVIVAL
        player.inventory.helmet = ItemBuilder(Material.LEATHER_HELMET).metaColor(getArmorColor(player)).build()
        player.inventory.chestplate = ItemBuilder(Material.LEATHER_CHESTPLATE).metaColor(getArmorColor(player)).build()
        player.inventory.leggings = ItemBuilder(Material.LEATHER_LEGGINGS).metaColor(getArmorColor(player)).build()
        player.inventory.boots = ItemBuilder(Material.LEATHER_BOOTS).metaColor(getArmorColor(player)).build()
        player.inventory.addItem(ItemBuilder(Material.WOODEN_SWORD).build())
    }

    private fun getArmorColor(player: Player): Color {
        return when(playerColors[player]){
            "BLUE" -> Color.BLUE
            "RED" -> Color.RED
            "YELLOW" -> Color.YELLOW
            "LIME" -> Color.LIME
            else -> {
                println(playerColors[player])
                Color.TEAL}
        }
    }

    override fun save(createSection: ConfigurationSection) {
        super.save(createSection)
        createSection["generator"] = generators
    }

    fun openBoutique(sender: Player) {
        playerEquipement[sender]?.let {
            BedwarsShopMenu(it).open(sender)
        }
    }

    fun openUpgradeBoutique(player: Player) {
        playerEquipement[player]?.let {
            BedwarsUpgradeMenu(it).open(player)
        }
    }


}