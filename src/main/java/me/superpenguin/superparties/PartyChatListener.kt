package me.superpenguin.superparties

import com.github.supergluelib.teams.TeamManager.getTeam
import me.superpenguin.superglue.foundations.send
import me.superpenguin.superglue.foundations.toColour
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.AsyncPlayerChatEvent

class PartyChatListener(val parties: PartyPlugin): Listener {

    @EventHandler fun onChat(event: AsyncPlayerChatEvent) {
        val sender = event.player
        if (parties.partyCommand.toggledPChat.contains(sender.uniqueId)) {
            event.isCancelled = true
            sender.getTeam()?.messageAll("&8[&9Party Chat&8]&7${sender.name}&f: ${event.message}".toColour())
                ?: parties.partyCommand.toggledPChat.remove(sender.uniqueId).also {
                    sender.send("&cToggled party chat off as you are no longer in a party!")
                }
        }
    }
}