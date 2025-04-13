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

class BedwarsShopMenu(val bedwarsPlayerEquipement: BedwarsPlayerEquipement) :
        Menu(MenuSize.Six, Component.text("Boutique", NamedTextColor.GOLD)) {

    override fun placeButtons(viewer: Player) {
        super.placeButtons(viewer)

        fill(
            ItemBuilder(Material.GRAY_STAINED_GLASS_PANE).name("")
                    .build())
        val playerInv = viewer.inventory


        addBlock(9, Material.valueOf(bedwarsPlayerEquipement.color + "_WOOL"), 16, 8, Material.COPPER_INGOT, playerInv)
        addBlock(18, Material.OAK_PLANKS, 16, 16, Material.COPPER_INGOT, playerInv)
        addBlock(27, Material.END_STONE, 4, 8, Material.IRON_INGOT, playerInv)
        addBlock(36, Material.OBSIDIAN, 2, 2, Material.EMERALD, playerInv)

        addEquipement(playerInv)

        addSword(13, Material.STONE_SWORD, 16, Material.COPPER_INGOT, playerInv)
        addSword(22, Material.IRON_SWORD, 8, Material.IRON_INGOT, playerInv)
        addSword(31, Material.DIAMOND_SWORD, 4, Material.EMERALD, playerInv)
        addButton(
            40,
            ItemBuilder(Material.STICK).name("Baton qui pousse")
                    .lore()
                    .newline()
                    .append("Prix : ", NamedTextColor.GOLD)
                    .append("4 ${getItemText(Material.GOLD_INGOT)}")
                    .buildLore()
                    .addEnchantment(Enchantment.KNOCKBACK, 1)
                    .build()) {
            if (playerInv.containsAtLeast(ItemBuilder(Material.GOLD_INGOT).build(), 4)) {
                playerInv.removeItem(
                    ItemBuilder(Material.GOLD_INGOT).amount(4)
                            .build())
                playerInv.addItem(
                    ItemBuilder(Material.STICK).addEnchantment(Enchantment.KNOCKBACK, 1)
                            .build())
            }
        }

        addBow(playerInv)

        var ibPick = ItemBuilder(bedwarsPlayerEquipement.pickaxeMaterial).lore().newline()
                .append("Prix : ", NamedTextColor.GOLD)
                .append("2 ${getItemText(Material.GOLD_INGOT)}").buildLore()
        bedwarsPlayerEquipement.pickaxeEnchantment.forEach { ibPick.addEnchantment(it.key,it.value)  }

        addButton(17,ibPick.build()){
            if (playerInv.containsAtLeast(ItemBuilder(Material.GOLD_INGOT).build(), 2)) {
                playerInv.removeItem(
                    ItemBuilder(Material.GOLD_INGOT).amount(2)
                            .build())
                var ibPick2 = ItemBuilder(bedwarsPlayerEquipement.pickaxeMaterial)
                bedwarsPlayerEquipement.pickaxeEnchantment.forEach { ibPick2.addEnchantment(it.key,it.value)  }
                playerInv.addItem(
                    ibPick2.build())
            }
        }

        addButton(26,ItemBuilder(Material.SHEARS).lore().newline()
                .append("Prix : ", NamedTextColor.GOLD)
                .append("5 ${getItemText(Material.IRON_INGOT)}").buildLore().build()){
            if (playerInv.containsAtLeast(ItemBuilder(Material.IRON_INGOT).build(), 5)) {
                playerInv.removeItem(
                    ItemBuilder(Material.IRON_INGOT).amount(5)
                            .build())
                playerInv.addItem(
                    ItemBuilder(Material.SHEARS).build())
            }
        }


    }

    private fun addBow(playerInv: PlayerInventory) {
        addButton(
            15,
            ItemBuilder(Material.BOW).lore()
                    .newline()
                    .append("Prix : ", NamedTextColor.GOLD)
                    .append("8 ${getItemText(Material.IRON_INGOT)}")
                    .buildLore()
                    .addEnchantment(Enchantment.INFINITY,1)
                    .build()) {
            if (playerInv.containsAtLeast(ItemBuilder(Material.IRON_INGOT).build(), 8)) {
                playerInv.removeItem(
                    ItemBuilder(Material.IRON_INGOT).amount(8)
                            .build())
                playerInv.addItem(
                    ItemBuilder(Material.BOW).addEnchantment(Enchantment.INFINITY, 1)
                            .build())
            }
        }
        addButton(
                24,
        ItemBuilder(Material.BOW).lore()
                .newline()
                .append("Prix : ", NamedTextColor.GOLD)
                .append("4 ${getItemText(Material.GOLD_INGOT)}")
                .buildLore()
                .addEnchantment(Enchantment.INFINITY,1)
                .addEnchantment(Enchantment.POWER,1)
                .build()) {
            if (playerInv.containsAtLeast(ItemBuilder(Material.GOLD_INGOT).build(), 4)) {
                playerInv.removeItem(
                    ItemBuilder(Material.GOLD_INGOT).amount(4)
                            .build())
                playerInv.addItem(
                    ItemBuilder(Material.BOW).addEnchantment(Enchantment.INFINITY, 1)
                            .addEnchantment(Enchantment.POWER,1)
                            .build())
            }
        }
        addButton(
                33,
        ItemBuilder(Material.BOW).lore()
                .newline()
                .append("Prix : ", NamedTextColor.GOLD)
                .append("5 ${getItemText(Material.EMERALD)}")
                .buildLore()
                .addEnchantment(Enchantment.INFINITY,1)
                .addEnchantment(Enchantment.POWER,1)
                .addEnchantment(Enchantment.PUNCH,1)
                .build()) {
            if (playerInv.containsAtLeast(ItemBuilder(Material.EMERALD).build(), 5)) {
                playerInv.removeItem(
                    ItemBuilder(Material.EMERALD).amount(5)
                            .build())
                playerInv.addItem(
                    ItemBuilder(Material.BOW).addEnchantment(Enchantment.INFINITY, 1)
                            .addEnchantment(Enchantment.POWER,1)
                            .addEnchantment(Enchantment.PUNCH,1)
                            .build())
            }
        }

        addButton(
            42,
            ItemBuilder(Material.ARROW).lore()
                    .newline()
                    .append("Prix : ", NamedTextColor.GOLD)
                    .append("1 ${getItemText(Material.GOLD_INGOT)}")
                    .buildLore()
                    .build()) {
            if (playerInv.containsAtLeast(ItemBuilder(Material.GOLD_INGOT).build(), 1)) {
                playerInv.removeItem(
                    ItemBuilder(Material.GOLD_INGOT).amount(1)
                            .build())
                playerInv.addItem(
                    ItemBuilder(Material.ARROW)
                            .build())
            }
        }
    }

    private fun addSword(index: Int, mat: Material, price: Int, priceItem: Material, playerInv: PlayerInventory) {
        addButton(
            index,
            ItemBuilder(mat)
                    .lore()
                    .newline()
                    .append("Prix : ", NamedTextColor.GOLD)
                    .append("$price ${getItemText(priceItem)}", NamedTextColor.DARK_RED)
                    .buildLore()
                    .build()) {
            if (playerInv.containsAtLeast(ItemBuilder(priceItem).build(), price)) {
                playerInv.removeItem(
                    ItemBuilder(priceItem).amount(price)
                            .build())
                var ib = ItemBuilder(mat)
                bedwarsPlayerEquipement.swordEnchant.forEach {
                    ib.addEnchantment(it.key, it.value)
                }
                playerInv.addItem(ib.build())
            }
        }
    }

    private fun addEquipement(playerInv: PlayerInventory) {
        if (bedwarsPlayerEquipement.hasPurchasedMail) {
            addButton(
                11,
                ItemBuilder(Material.BARRIER).name("Équipement en mailles")
                        .lore()
                        .newline()
                        .append("Vous avez déjà acheté cela.")
                        .buildLore()
                        .build())
        } else {
            addButton(
                11,
                ItemBuilder(Material.CHAINMAIL_BOOTS).name("Équipement en mailles")
                        .lore()
                        .newline()
                        .append("Prix : ", NamedTextColor.GOLD)
                        .append("8 ${getItemText(Material.IRON_INGOT)}", NamedTextColor.DARK_RED)
                        .buildLore()
                        .build()) {
                if (playerInv.containsAtLeast(ItemBuilder(Material.IRON_INGOT).build(), 8)) {
                    playerInv.removeItem(
                        ItemBuilder(Material.IRON_INGOT).amount(8)
                                .build())
                    var ibBoots = ItemBuilder(Material.CHAINMAIL_BOOTS)
                    bedwarsPlayerEquipement.armorEnchant.forEach {
                        ibBoots = ibBoots.addEnchantment(it.key, it.value)
                    }
                    playerInv.boots = ibBoots.build()
                    var ibLegs = ItemBuilder(Material.CHAINMAIL_LEGGINGS)
                    bedwarsPlayerEquipement.armorEnchant.forEach {
                        ibLegs = ibLegs.addEnchantment(it.key, it.value)
                    }
                    playerInv.leggings = ibLegs.build()
                    bedwarsPlayerEquipement.hasPurchasedMail = true
                }
            }
        }

        if (bedwarsPlayerEquipement.hasPurchasedIron) {
            addButton(
                20,
                ItemBuilder(Material.BARRIER).name("Équipement en fer")
                        .lore()
                        .newline()
                        .append("Vous avez déjà acheté cela.")
                        .buildLore()
                        .build())
        } else {
            addButton(
                20,
                ItemBuilder(Material.IRON_CHESTPLATE).name("Équipement en fer")
                        .lore()
                        .newline()
                        .append("Prix : ", NamedTextColor.GOLD)
                        .append("4 ${getItemText(Material.GOLD_INGOT)}", NamedTextColor.DARK_RED)
                        .buildLore()
                        .build()) {
                if (playerInv.containsAtLeast(ItemBuilder(Material.GOLD_INGOT).build(), 4)) {
                    playerInv.removeItem(
                        ItemBuilder(Material.GOLD_INGOT).amount(4)
                                .build())
                    var ib = ItemBuilder(Material.IRON_CHESTPLATE)
                    bedwarsPlayerEquipement.armorEnchant.forEach {
                        ib = ib.addEnchantment(it.key, it.value)
                    }
                    playerInv.chestplate = ib.build()
                    bedwarsPlayerEquipement.hasPurchasedIron = true
                }

            }
        }


        if (bedwarsPlayerEquipement.hasPurchasedDiamond) {
            addButton(
                29,
                ItemBuilder(Material.BARRIER).name("Équipement en diamant")
                        .lore()
                        .newline()
                        .append("Vous avez déjà acheté cela.")
                        .buildLore()
                        .build())
        } else {
            addButton(
                29,
                ItemBuilder(Material.DIAMOND_CHESTPLATE).name("Équipement en diamant")
                        .lore()
                        .newline()
                        .append("Prix : ", NamedTextColor.GOLD)
                        .append("4 ${getItemText(Material.EMERALD)}", NamedTextColor.DARK_RED)
                        .buildLore()
                        .build()) {
                if (playerInv.containsAtLeast(ItemBuilder(Material.EMERALD).build(), 4)) {
                    playerInv.removeItem(
                        ItemBuilder(Material.EMERALD).amount(4)
                                .build())
                    var ib = ItemBuilder(Material.DIAMOND_CHESTPLATE)
                    bedwarsPlayerEquipement.armorEnchant.forEach {
                        ib = ib.addEnchantment(it.key, it.value)
                    }
                    playerInv.chestplate = ib.build()
                    bedwarsPlayerEquipement.hasPurchasedIron = true
                    bedwarsPlayerEquipement.hasPurchasedDiamond = true
                }

            }
        }
    }

    private fun addBlock(
        index: Int,
        itemMaterial: Material,
        itemAmount: Int,
        itemPrice: Int,
        itemPriceItem: Material,
        playerInv: PlayerInventory,
    ) {
        addButton(
            index,
            ItemBuilder(itemMaterial).amount(itemAmount)
                    .lore()
                    .newline()
                    .append("Prix : ", NamedTextColor.GOLD)
                    .append("$itemPrice ${getItemText(itemPriceItem)}", NamedTextColor.DARK_RED)
                    .buildLore()
                    .build()) {
            if (playerInv.containsAtLeast(ItemBuilder(itemPriceItem).build(), itemPrice)) {
                playerInv.removeItem(
                    ItemBuilder(itemPriceItem).amount(itemPrice)
                            .build())
                playerInv.addItem(
                    ItemBuilder(itemMaterial).amount(itemAmount)
                            .build())
            }
        }
    }

    private fun getItemText(itemMaterial: Material): String {
        return when (itemMaterial) {
            Material.COPPER_INGOT -> "cuivres"
            Material.IRON_INGOT -> "lingots de fer"
            Material.EMERALD -> "emeraudes"
            Material.GOLD_INGOT -> "lingots d'or"
            else -> itemMaterial.name
        }
    }


}