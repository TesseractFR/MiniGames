package onl.tesseract.miniGames.minigames.bedwars.menu

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import onl.tesseract.lib.menu.ItemBuilder
import onl.tesseract.lib.menu.Menu
import onl.tesseract.lib.menu.MenuSize
import onl.tesseract.miniGames.minigames.bedwars.BedwarsPlayerEquipement
import org.bukkit.Material
import org.bukkit.enchantments.Enchantment
import org.bukkit.entity.Player
import org.bukkit.inventory.PlayerInventory

class BedwarsUpgradeMenu(val bedwarsPlayerEquipement: BedwarsPlayerEquipement) :
        Menu(MenuSize.Three, Component.text("Amélioration", NamedTextColor.GOLD)) {

    override fun placeButtons(viewer: Player) {
        super.placeButtons(viewer)

        fill(
            ItemBuilder(Material.GRAY_STAINED_GLASS_PANE).name("")
                    .build())
        val playerInv = viewer.inventory

        addPickaxeMatUpgrade(10, playerInv)
        addPickaxeEnchantUpgrade(12, playerInv)
        addSwordEnchantUpgrade(14, playerInv)
        addArmorEnchantUpgrade(16, playerInv)
    }

    private fun addPickaxeMatUpgrade(index: Int, playerInv: PlayerInventory) {
        val name = "Amélioration de la pioche"
        val arg : Triple<String,Material,Int> =
            when (bedwarsPlayerEquipement.pickaxeMaterial) {
            Material.WOODEN_PICKAXE -> Triple("pierre", Material.STONE_PICKAXE,2)
            Material.STONE_PICKAXE -> Triple("fer",Material.IRON_PICKAXE,4)
            Material.IRON_PICKAXE -> Triple("diamant", Material.DIAMOND_PICKAXE,8)
            else -> {
                addFullUpgrade(index, name)
                return
            }
        }
        addButton(index,
            ItemBuilder(arg.second).name(name)
                    .lore()
                    .newline()
                    .append("Améliore les pioches de votre boutique en pioche en ${arg.first}.",NamedTextColor.DARK_GREEN).newline()
                    .append("Prix :",NamedTextColor.GOLD).newline()
                    .append("${arg.third} diamants",NamedTextColor.YELLOW)
                    .buildLore()
                    .build()) {
            checkAndBuy(arg.third, Material.DIAMOND, playerInv) {
                bedwarsPlayerEquipement.pickaxeMaterial = arg.second
            }
        }
    }
    private fun addPickaxeEnchantUpgrade(index: Int, playerInv: PlayerInventory) {
        val name = "Amélioration de la pioche"
        val arg : Pair<Int,Int> =
            when (bedwarsPlayerEquipement.pickaxeEnchantment[Enchantment.EFFICIENCY]) {
            null -> Pair(1,2)
            1 -> Pair(2,4)
            2 -> Pair(3,8)
            3 -> Pair(4,16)
            else -> {
                addFullUpgrade(index, name)
                return
            }
        }
        addButton(index,
            ItemBuilder(bedwarsPlayerEquipement.pickaxeMaterial).name(name)
                    .lore()
                    .newline()
                    .append("Améliore les pioches de votre boutique avec l'enchantement efficacité ${arg.first}.",NamedTextColor.DARK_GREEN).newline()
                    .append("Prix :",NamedTextColor.GOLD).newline()
                    .append("${arg.second} diamants",NamedTextColor.YELLOW)
                    .buildLore()
                    .build()) {
            checkAndBuy(arg.second, Material.DIAMOND, playerInv) {
                bedwarsPlayerEquipement.pickaxeEnchantment[Enchantment.EFFICIENCY] = arg.first
            }
        }
    }
    private fun addSwordEnchantUpgrade(index: Int, playerInv: PlayerInventory) {
        val name = "Amélioration de l'épée"
        val arg : Pair<Int,Int> =
            when (bedwarsPlayerEquipement.swordEnchant[Enchantment.SHARPNESS]) {
            null -> Pair(1,2)
            1 -> Pair(2,4)
            2 -> Pair(3,8)
            else -> {
                addFullUpgrade(index, name)
                return
            }
        }
        addButton(index,
            ItemBuilder(Material.GOLDEN_SWORD).name(name)
                    .lore()
                    .newline()
                    .append("Améliore les épées de votre boutique avec l'enchantement tranchant ${arg.first}.",NamedTextColor.DARK_GREEN).newline()
                    .append("Prix :",NamedTextColor.GOLD).newline()
                    .append("${arg.second} diamants",NamedTextColor.YELLOW)
                    .buildLore()
                    .build()) {
            checkAndBuy(arg.second, Material.DIAMOND, playerInv) {
                bedwarsPlayerEquipement.swordEnchant[Enchantment.SHARPNESS] = arg.first
            }
        }
    }
    private fun addArmorEnchantUpgrade(index: Int, playerInv: PlayerInventory) {
        val name = "Amélioration de l'armure"
        val arg : Pair<Int,Int> =
            when (bedwarsPlayerEquipement.armorEnchant[Enchantment.PROTECTION]) {
            null -> Pair(1,2)
            1 -> Pair(2,4)
            2 -> Pair(3,8)
            3 -> Pair(4,16)
            else -> {
                addFullUpgrade(index, name)
                return
            }
        }
        addButton(index,
            ItemBuilder(Material.GOLDEN_CHESTPLATE).name(name)
                    .lore()
                    .newline()
                    .append("Améliore les épées de votre boutique avec l'enchantement tranchant ${arg.first}.",NamedTextColor.DARK_GREEN).newline()
                    .append("Prix :",NamedTextColor.GOLD).newline()
                    .append("${arg.second} diamants",NamedTextColor.YELLOW)
                    .buildLore()
                    .build()) {
            checkAndBuy(arg.second, Material.DIAMOND, playerInv) {
                playerInv.helmet?.removeEnchantments()
                playerInv.helmet?.addUnsafeEnchantment(Enchantment.PROTECTION,arg.first)
                playerInv.chestplate?.removeEnchantments()
                playerInv.chestplate?.addUnsafeEnchantment(Enchantment.PROTECTION,arg.first)
                playerInv.leggings?.removeEnchantments()
                playerInv.leggings?.addUnsafeEnchantment(Enchantment.PROTECTION,arg.first)
                playerInv.boots?.removeEnchantments()
                playerInv.boots?.addUnsafeEnchantment(Enchantment.PROTECTION,arg.first)
                bedwarsPlayerEquipement.armorEnchant[Enchantment.PROTECTION] = arg.first
            }
        }
    }


    private fun addFullUpgrade(index: Int, name: String) {
        addButton(index,ItemBuilder(Material.BARRIER).name(name).color(NamedTextColor.RED).lore().newline().append("Vous avez déjà totalement amélioré ceci.",NamedTextColor.GRAY).buildLore().build())
    }

    private fun checkAndBuy(price: Int, priceMat: Material, playerInv: PlayerInventory, function: () -> Unit) {
        if (playerInv.containsAtLeast(ItemBuilder(priceMat).build(), price)) {
            playerInv.removeItem(
                ItemBuilder(priceMat).amount(price)
                        .build())
            function()
            this.close()
        }
    }
}