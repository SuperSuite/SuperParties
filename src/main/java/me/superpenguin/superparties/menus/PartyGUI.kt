package me.superpenguin.superparties.menus

import me.superpenguin.superglue.foundations.util.ItemBuilder
import me.superpenguin.superglue.guis.GUI
import me.superpenguin.superglue.guis.Panes
import me.superpenguin.superglue.guis.setBorder
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.Inventory

class PartyGUI(val player: Player): GUI() {

    companion object {
        private val button_members = ItemBuilder(Material.OAK_SIGN, "&bMembers")
            .addLore("&7Click here to view your party members")
            .build()
    }

    override fun generateInventory(): Inventory {
        return createInventory("&bYour party", 27) {
            setBorder(Panes.BLACK)
//            setGUIButton(10, button_members) {
//                val items = player.getTeam()!!.players.map {
//                    ItemBuilder(Material.PLAYER_HEAD, "&b${it.name}")
//                        .skullOwner(it.uniqueId)
//                        .locname(player.uniqueId.toString())
//                        .build()
//              }
//            }

        }
    }
}