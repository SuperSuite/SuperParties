package me.superpenguin.superparties

import com.github.supergluelib.lamp.annotations.NotSelf
import me.superpenguin.superglue.foundations.send
import me.superpenguin.superglue.foundations.toColor
import me.superpenguin.superparties.menus.NoPartyGUI
import me.superpenguin.superparties.menus.PartyGUI
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.event.ClickEvent
import net.kyori.adventure.text.event.HoverEvent
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextDecoration
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import revxrsal.commands.annotation.AutoComplete
import revxrsal.commands.annotation.Command
import revxrsal.commands.annotation.DefaultFor
import revxrsal.commands.annotation.Named
import revxrsal.commands.annotation.Subcommand
import revxrsal.commands.bukkit.player
import revxrsal.commands.exception.CommandErrorException
import java.util.*

@Command("party")
class PartyCommand(val parties: SuperParties.PartyPlugin) {
    val manager = parties.manager
    val handler = parties.commandHandler

    @Target(AnnotationTarget.VALUE_PARAMETER)
    @Retention(AnnotationRetention.RUNTIME)
    private annotation class Partyless

    @Target(AnnotationTarget.VALUE_PARAMETER)
    @Retention(AnnotationRetention.RUNTIME)
    private annotation class Partied

    @Target(AnnotationTarget.VALUE_PARAMETER)
    @Retention(AnnotationRetention.RUNTIME)
    private annotation class PartyLeader

    init {
        handler.autoCompleter.registerSuggestion("TeamMembers") { _, sender, _ -> manager.getTeam(sender.player)?.getNames() ?: throw CommandErrorException("You are not in a team") }

        handler.registerParameterValidator(Player::class.java) { player, param, actor ->
            if (param.hasAnnotation(Partyless::class.java)) {
                if (manager.hasTeam(player)) throw CommandErrorException("You are already in a party")
            } else if (param.hasAnnotation(Partied::class.java)) {
                if (!manager.hasTeam(player)) throw CommandErrorException("You are not in a party")
            }
            if (param.hasAnnotation(PartyLeader::class.java)) {
                val team = manager.getTeam(player) ?: throw CommandErrorException("You are not in a party")
                if (!team.isLeader(player)) throw CommandErrorException("You are not the party leader")
            }
        }
    }

    @DefaultFor("party")
    fun onLonelyArg(sender: Player) {
        if (manager.hasTeam(sender)) PartyGUI(sender, manager.getTeam(sender)!!).open(sender)
        else NoPartyGUI(sender).open(sender)
    }

    @Subcommand("create")
    fun createParty(@Partyless sender: Player) {
        manager.addTeam(Party(sender))
        sender.send("&7Created a new party")
    }

    @Subcommand("invite")
    fun inviteToParty(sender: Player, @NotSelf @Named("invitee") other: Player) {
        val team = manager.getOrCreateTeam(sender) { Party(sender) }
        val teamid = team.id
        sender.send("&7Sent an invite to ${other.name}")
        parties.audience.player(other).sendMessage(
            Component.text("${sender.name} has sent you a party invite. ").color(NamedTextColor.GRAY).append(
                Component.text("Click here to accept")
                    .color(NamedTextColor.GOLD)
                    .decorate(TextDecoration.BOLD)
                    .hoverEvent(HoverEvent.showText(Component.text("Click here to accept ${sender.name}'s invite.")))
                    .clickEvent(ClickEvent.callback { manager.findTeamByID(teamid)?.add(other) ?: other.send("&cThis party has expired") })
            )
        )
    }

    @Subcommand("leave")
    fun leaveParty(@Partied sender: Player) {
        val team = manager.removePlayerFromTeam(sender)
        parties.toggledPChat.remove(sender.uniqueId)
        team?.messageAll("&9${sender.name}&7 has left the party".toColor())
        if (team != null && team.isLeader(sender)) {
            team.leader = team.getUUIDs().first()
            team.messageAll("&9${Bukkit.getOfflinePlayer(team.leader).name!!}&7 has been promoted to party leader")
        }
        sender.send("&7Left your current party")
    }

    @Subcommand("chat")
    fun toggleChat(@Partied sender: Player) {
        val uuid = sender.uniqueId
        if (parties.toggledPChat.contains(uuid)) {
            parties.toggledPChat.remove(uuid)
            sender.send("&7Turned &coff&7 party chat!")
        } else {
            parties.toggledPChat.add(uuid)
            sender.send("&7Turned &aon&7 party chat!")
        }
    }

    @Subcommand("kick")
    @AutoComplete("@TeamMembers")
    fun kickPlayer(@PartyLeader sender: Player, other: Player) {
        if (sender == other) throw CommandErrorException("Use '/party leave' instead")
        val team = manager.getTeam(sender)!!
        if (team.remove(other)) sender.send("&7Kicked ${other.name} from the party")
        else sender.send("&7${other.name} is not in your party")
    }



}