package onl.tesseract.miniGames.minigames.quake

import com.destroystokyo.paper.ParticleBuilder
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import onl.tesseract.miniGames.QUAKE
import onl.tesseract.miniGames.minigames.ARENA
import onl.tesseract.miniGames.minigames.MiniGameMap
import onl.tesseract.miniGames.minigames.PLAYSPAWN
import onl.tesseract.miniGames.minigames.SPAWN
import onl.tesseract.miniGames.utils.cuboid.Cuboid
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.Particle
import org.bukkit.configuration.ConfigurationSection
import org.bukkit.entity.Entity
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.util.Vector


class QuakeMap(name: String, spawn: Location) : MiniGameMap(name, spawn, QUAKE) {
    init {
        lifeSystem = true
        maxLife = 10
    }
    override fun playerLeaveArena(player: Player) {
        TODO("Not yet implemented")
    }

    override fun getHeaderComponent(): Component {
        return Component.text("[", NamedTextColor.GOLD)
                .append(Component.text("Quake", NamedTextColor.AQUA))
                .append(Component.text("] ", NamedTextColor.GOLD))
    }

    private fun playerTouch(player: Player,target: Player) {
        playerKilled(target,player)
        target.teleport(getRandomSpawn())
        target.maxHealth = playersLifes[target]?.times(2.0)?:20.0
    }

    @EventHandler
    fun onPlayerUseHoe(event: PlayerInteractEvent) {
        val player = event.player
        if (player !in players) {
            return
        }
        if(player.attackCooldown<1){
            return
        }
        val e: Entity? = player.getTargetEntity(100)

        val target = e?.location?: player.getTargetBlock(null, 100).location
        val v: Vector = target.clone().subtract(player.location)
                .toVector()
                .normalize()
                .multiply(0.3f)
        val distance: Double = target.distance(player.location)
        var i = 0f
        val particleLocation = player.location
                .add(0.0,1.5,0.0)
        while (i < distance) {
            ParticleBuilder(Particle.DUST)
                    .color(102, 255, 255)
                    .count(1)
                    .location(particleLocation)
                    .spawn()
            ParticleBuilder(Particle.DUST).color(229, 204, 255)
                    .count(1)
                    .location(particleLocation)
                    .spawn()
            ParticleBuilder(Particle.DUST).color(153, 255, 153)
                    .count(1)
                    .location(particleLocation)
                    .spawn()

            particleLocation.add(v)
            i += 0.3.toFloat()
        }
        if(e != null && e is Player) {
            playerTouch(player,e)
        }

    }

    companion object {
        fun of(configurationSection: ConfigurationSection): MiniGameMap {
            val name = configurationSection.name
            val spawn = configurationSection.getLocation(SPAWN) ?: Bukkit.getWorlds()[0].spawnLocation
            val map = QuakeMap(name, spawn)
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