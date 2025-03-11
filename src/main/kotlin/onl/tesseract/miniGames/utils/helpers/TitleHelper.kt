package onl.tesseract.miniGames.utils.helpers

import net.kyori.adventure.text.Component
import net.kyori.adventure.title.TitlePart
import org.bukkit.entity.Player



object TitleHelper {


    fun sendTitle(player: Player, title: Component, subtitle: Component) {
        player.sendTitlePart(TitlePart.TITLE, title)
        player.sendTitlePart(TitlePart.SUBTITLE, subtitle)
    }

    fun clearTitle(player: Player){
        player.clearTitle()
    }

}