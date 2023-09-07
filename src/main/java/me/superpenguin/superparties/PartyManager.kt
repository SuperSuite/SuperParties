package me.superpenguin.superparties

import com.github.supergluelib.teams.TeamManager
import org.bukkit.Bukkit
import org.bukkit.entity.Player

class PartyManager(val superParties: SuperParties): TeamManager<Party>(superParties) {

    override fun removePlayerFromTeam(player: Player): Party? {
        val team = super.removePlayerFromTeam(player)
        superParties.parties.toggledPChat.remove(player.uniqueId)
        if (team != null && team.isLeader(player)) {
            team.leader = team.getUUIDs().first()
            team.messageAll("&9${Bukkit.getOfflinePlayer(team.leader).name!!}&7 has been promoted to party leader")
        }
        return team
    }

}