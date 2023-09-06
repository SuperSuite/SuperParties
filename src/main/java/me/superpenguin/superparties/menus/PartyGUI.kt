package me.superpenguin.superparties.menus

import com.github.supergluelib.guis.GUI
import com.github.supergluelib.guis.Panes
import com.github.supergluelib.guis.setBorder
import com.github.supergluelib.guis.templates.DisplayGUI
import me.superpenguin.superglue.foundations.util.ItemBuilder
import me.superpenguin.superparties.Party
import me.superpenguin.superparties.SuperParties
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.Inventory
import java.util.*

class PartyGUI(val player: Player, val party: Party): GUI() {
    val manager = SuperParties.instance.parties.manager

    companion object {
        private val button_members = ItemBuilder(Material.OAK_SIGN, "&bMembers")
            .addLore("&7Click here to view your party members")
            .build()
    }

    private fun getMemberItems() = party.players.map { member ->
        ItemBuilder(Material.PLAYER_HEAD, "&b${member.name}")
            .skullOwner(member.uniqueId)
            .locname(player.uniqueId.toString())
            .build()
    }

    override fun generateInventory(): Inventory {
        return createInventory("&bYour party", 27) {
            setBorder(Panes.BLACK)
            setGUIButton(10, button_members) {
                DisplayGUI("&bYour party members", getMemberItems()) {
                    val uuid = locname?.let { UUID.fromString(it) } ?: return@DisplayGUI
                    val temp = ItemBuilder(Material.GREEN_WOOL, "&aPOOF").build()
                    event.inventory.setTemporaryButton(event.slot, 40, temp)
                }.setBackButton(18, this@PartyGUI)
            }
        }
    }

}