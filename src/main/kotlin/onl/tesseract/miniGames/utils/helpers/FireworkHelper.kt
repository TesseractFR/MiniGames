package onl.tesseract.miniGames.utils.helpers

import org.bukkit.Color
import org.bukkit.FireworkEffect
import org.bukkit.entity.Firework
import org.bukkit.entity.Player
import kotlin.random.Random

object FireworkHelper {
    fun fireWorkEffect(paramPlayer: Player) {
        val firework = paramPlayer.world.spawn(
            paramPlayer.location.add(0.0, 1.0, 0.0),
            Firework::class.java)
        val fireworkMeta = firework.fireworkMeta
        var random = Random(System.currentTimeMillis())
        val fireworkEffect: FireworkEffect = FireworkEffect.builder()
                .flicker(random.nextBoolean())
                .withColor(Color.fromBGR(random.nextInt(256), random.nextInt(256), random.nextInt(256)))
                .withFade(Color.fromBGR(random.nextInt(256), random.nextInt(256), random.nextInt(256)))
                .with(
                    FireworkEffect.Type.entries.toTypedArray()
                            .get(random.nextInt((FireworkEffect.Type.entries.toTypedArray()).size)))
                .trail(random.nextBoolean())
                .build()
        fireworkMeta.addEffect(fireworkEffect)
        firework.fireworkMeta = fireworkMeta
    }
}