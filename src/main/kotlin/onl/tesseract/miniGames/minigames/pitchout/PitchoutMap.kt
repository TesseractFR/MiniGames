package onl.tesseract.miniGames.minigames.pitchout

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import onl.tesseract.lib.menu.ItemBuilder
import onl.tesseract.lib.util.append
import onl.tesseract.miniGames.PITCHOUT
import onl.tesseract.miniGames.minigames.ARENA
import onl.tesseract.miniGames.minigames.MiniGameMap
import onl.tesseract.miniGames.minigames.PLAYSPAWN
import onl.tesseract.miniGames.minigames.SPAWN
import onl.tesseract.miniGames.utils.cuboid.Cuboid
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.configuration.ConfigurationSection
import org.bukkit.enchantments.Enchantment.*
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType

class PitchoutMap(name: String, spawn: Location) : MiniGameMap(name, spawn, PITCHOUT) {
    init {
        maxLife = 5
        lifeSystem = true
        pvpEnabled = true
    }

    override fun start() {
        super.start()
        for(p in players){
            p.addPotionEffect(PotionEffect(PotionEffectType.SATURATION, PotionEffect.INFINITE_DURATION,0,false,false))
            p.addPotionEffect(PotionEffect(PotionEffectType.RESISTANCE, PotionEffect.INFINITE_DURATION,1,false,false))
            p.addPotionEffect(PotionEffect(PotionEffectType.REGENERATION, PotionEffect.INFINITE_DURATION,1,false,false))
        }
    }

    override fun playerLeaveArena(player: Player) {
        playersLifes[player] = playersLifes[player]?.minus(1)?:maxLife
        val lastDamager = player.lastDamageCause?.damageSource?.causingEntity
        if(lastDamager != null)
        {
            val killer = lastDamager as Player
            playerKills[killer] = playerKills[killer]?.plus(1)?:1
            displayPlayerKilled(player, killer)
        }
        else{
            broadcast(getDisplayPlayerComponent(player).append(" est tombé.",NamedTextColor.YELLOW))
        }
        if(playersLifes[player]!! <=0){
            eliminatePlayer(player)
        }
        else{
            player.teleport(getRandomSpawn())
        }
    }

    override fun getRankingDetail(player: Player): Component {
        return Component.text(" (${playerKills[player]?:0} victimes)", NamedTextColor.GRAY)
    }

    override fun getHeaderComponent(): Component {
        return Component.text("[", NamedTextColor.GOLD)
                .append(Component.text("Pitch Out", NamedTextColor.AQUA))
                .append(Component.text("] ", NamedTextColor.GOLD))
    }

    override fun setInventory(player: Player) {
        super.setInventory(player)
        giveStick(player)
        giveBow(player)
    }
    private fun giveBow(player: Player) {
        val bow = ItemBuilder(Material.BOW)
                .name(Component.text("[", NamedTextColor.GOLD)
                .append(Component.text("Piou Piou", NamedTextColor.GOLD))
                .append(Component.text("] ", NamedTextColor.GOLD)))
                .addEnchantment(PUNCH,3)
                .addEnchantment(INFINITY,1)
                .build()
        player.inventory.addItem(bow)
        player.inventory.addItem(ItemStack(Material.ARROW))
    }
    private fun giveStick(player: Player) {
        val stick = ItemBuilder(Material.STICK)
                .name(Component.text("[", NamedTextColor.GOLD)
                        .append("Gros bâton", NamedTextColor.YELLOW)
                        .append("]", NamedTextColor.GOLD))
                .addEnchantment(KNOCKBACK, 5)
                .build()
        player.inventory.addItem(stick)
    }

    companion object {
        fun of(configurationSection: ConfigurationSection): MiniGameMap {
            val name = configurationSection.name
            val spawn = configurationSection.getLocation(SPAWN) ?: Bukkit.getWorlds()[0].spawnLocation
            val map = PitchoutMap(name, spawn)
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

}