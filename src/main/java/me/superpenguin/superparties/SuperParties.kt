package me.superpenguin.superparties

import com.github.supergluelib.teams.TeamManager
import me.superpenguin.superglue.foundations.register
import me.superpenguin.superglue.foundations.send
import me.superpenguin.superglue.foundations.toColor
import me.superpenguin.superglue.guis.GUIManager
import net.kyori.adventure.platform.bukkit.BukkitAudiences
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.AsyncPlayerChatEvent
import org.bukkit.plugin.java.JavaPlugin
import revxrsal.commands.bukkit.BukkitCommandHandler
import java.util.*

class SuperParties: JavaPlugin(), Listener {

    companion object {
        lateinit var instance: SuperParties
            private set
    }

    /**
     * Instead of creating a bunch of 'lateinit var private sets', This data class will hold them.
     */
    data class PartyPlugin(
        val plugin: SuperParties,
        val manager: TeamManager<Party>,
        val commandHandler: BukkitCommandHandler,
        val audience: BukkitAudiences,
        val toggledPChat: HashSet<UUID>,
    )

    lateinit var parties: PartyPlugin
        private set

    override fun onEnable() {
        instance = this

        val cmdhandler = BukkitCommandHandler.create(this)
        val manager = TeamManager<Party>(this)

        parties = PartyPlugin(
            this,
            manager,
            cmdhandler,
            BukkitAudiences.create(this),
            HashSet(),
        )

        cmdhandler.register(PartyCommand(parties))
        register(this)

        GUIManager.setup(this)

        manager.allowFriendlyFire = false
    }

    @EventHandler fun onChat(event: AsyncPlayerChatEvent) {
        val sender = event.player
        if (parties.toggledPChat.contains(sender.uniqueId)) {
            event.isCancelled = true
            parties.manager.getTeam(sender)?.messageAll("&8[&9Party Chat&8]&7 ${sender.name}&f: ${event.message}".toColor())
                ?: parties.toggledPChat.remove(sender.uniqueId).also {
                    sender.send("&cToggled party chat off as you are no longer in a party!")
                }
        }
    }

}