package me.superpenguin.superparties

import com.github.supergluelib.teams.TeamManager
import me.superpenguin.superglue.foundations.register
import net.kyori.adventure.platform.bukkit.BukkitAudiences
import org.bukkit.plugin.java.JavaPlugin
import revxrsal.commands.bukkit.BukkitCommandHandler

class SuperParties: JavaPlugin() {

    lateinit var parties: PartyPlugin
        private set

    override fun onEnable() {
        val cmdhandler = BukkitCommandHandler.create(this)

        parties = PartyPlugin(
            this,
            BukkitAudiences.create(this),
            PartyCommand(cmdhandler, this)
        )

        cmdhandler.register(parties.partyCommand)
        PartyChatListener(parties).register(this)

        TeamManager.setup(this)
        TeamManager.allowFriendlyFire = false
    }
}