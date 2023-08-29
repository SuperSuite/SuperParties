package me.superpenguin.superparties

import com.github.supergluelib.teams.TeamManager
import com.github.supergluelib.teams.TeamManager.getTeam
import com.github.supergluelib.teams.TeamManager.hasTeam
import me.superpenguin.superglue.foundations.send
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.event.ClickEvent
import net.kyori.adventure.text.event.HoverEvent
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextDecoration
import org.bukkit.entity.Player
import revxrsal.commands.annotation.Command
import revxrsal.commands.annotation.NotSender
import revxrsal.commands.annotation.Subcommand
import revxrsal.commands.bukkit.BukkitCommandHandler
import revxrsal.commands.exception.CommandErrorException
import java.util.*

@Command("party")
class PartyCommand(handler: BukkitCommandHandler, val plugin: SuperParties) {

    @Target(AnnotationTarget.VALUE_PARAMETER)
    @Retention(AnnotationRetention.RUNTIME)
    private annotation class Partyless

    @Target(AnnotationTarget.VALUE_PARAMETER)
    @Retention(AnnotationRetention.RUNTIME)
    private annotation class Partied

    init {
        handler.registerParameterValidator(Player::class.java) { player, param, _ ->
            if (param.hasAnnotation(Partyless::class.java)) {
                if (player.hasTeam()) throw CommandErrorException("You are already in a party")
            } else if (param.hasAnnotation(Partied::class.java)) {
                if (!player.hasTeam()) throw CommandErrorException("You are not in a party")
            }
        }
    }

    val toggledPChat = HashSet<UUID>()

    @Subcommand("create")
    fun createParty(@Partyless sender: Player) {
        TeamManager.createNewTeam(sender)
        sender.send("&7Created a new party")
    }

    @Subcommand("invite")
    fun inviteToParty(sender: Player, @NotSender other: Player) {
        val team = TeamManager.getOrCreateTeam(sender)
        val teamid = team.id
        plugin.parties.audience.player(other).sendMessage(
            Component.text("${sender.name} has sent you a party invite. ").color(NamedTextColor.GRAY).append(
                Component.text("Click here to accept")
                    .color(NamedTextColor.GOLD)
                    .decorate(TextDecoration.BOLD)
                    .hoverEvent(HoverEvent.showText(Component.text("Click here to accept ${sender.name}'s invite.")))
                    .clickEvent(ClickEvent.callback { TeamManager.findTeamByID(teamid)?.add(other) ?: other.send("&cThis party has expired") })
            )
        )
    }

    @Subcommand("leave")
    fun leaveParty(@Partied sender: Player) {
        val team = sender.getTeam()!!
        team.remove(sender)
        if (team.isEmpty()) TeamManager.removeTeam(team)
        toggledPChat.remove(sender.uniqueId)
    }

    @Subcommand("chat")
    fun toggleChat(@Partied sender: Player) {
        val uuid = sender.uniqueId
        if (toggledPChat.contains(uuid)) {
            toggledPChat.remove(uuid)
            sender.send("&7Turned &coff&7 party chat!")
        } else {
            toggledPChat.add(uuid)
            sender.send("&7Turned &aon&7 party chat!")
        }
    }



}