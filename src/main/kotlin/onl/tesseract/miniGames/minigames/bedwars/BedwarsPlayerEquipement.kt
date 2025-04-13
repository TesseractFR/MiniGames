package onl.tesseract.miniGames.minigames.bedwars

import org.bukkit.Material
import org.bukkit.enchantments.Enchantment

data class BedwarsPlayerEquipement(
    val color : String,
    var hasPurchasedMail: Boolean = false,
    val armorEnchant: MutableMap<Enchantment, Int> = mutableMapOf(),
    var hasPurchasedIron: Boolean = false,
    var hasPurchasedDiamond : Boolean = false,
    val swordEnchant: MutableMap<Enchantment, Int> = mutableMapOf(),

    var pickaxeMaterial: Material = Material.WOODEN_PICKAXE,
    var pickaxeEnchantment:  MutableMap<Enchantment, Int> = mutableMapOf(),
    ) {
}