package me.superpenguin.superparties.menus

import com.github.supergluelib.guis.GUI
import com.github.supergluelib.guis.Panes
import com.github.supergluelib.guis.fillEmpty
import me.superpenguin.superglue.foundations.util.ItemBuilder
import me.superpenguin.superparties.Party
import me.superpenguin.superparties.SuperParties
import org.bukkit.Material
import org.bukkit.entity.Player

class NoPartyGUI(val player: Player): GUI() {
    val manager = SuperParties.instance.parties.manager

    companion object {
        val makePartyIcon = ItemBuilder(Material.LIME_STAINED_GLASS_PANE, "&aMake One?").build()
    }

    override fun generateInventory() = createInventory("&cYou don't have a party!", 45) {
        setDynamicButton(4) { Panes.random() }
        setGUIButton(22, makePartyIcon) {
            val party = manager.addTeam(Party(player))
            PartyGUI(player, party)
        }
        fillEmpty(Panes.BLACK)
    }
}