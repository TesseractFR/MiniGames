package onl.tesseract.miniGames.utils.helpers

import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextColor

object ComponentHelper {
    fun getColorForRank(rank: Int) : TextColor {
        return when(rank){
            1 -> NamedTextColor.GOLD
            2 -> NamedTextColor.YELLOW
            3 -> NamedTextColor.RED
            else -> NamedTextColor.GRAY
        }

    }
}