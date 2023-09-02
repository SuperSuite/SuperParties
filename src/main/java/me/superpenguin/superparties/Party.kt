package me.superpenguin.superparties

import com.github.supergluelib.teams.Team
import org.bukkit.entity.Player
import java.util.*

class Party(leader: Player): Team(hashSetOf(leader.uniqueId)) {
    var leader: UUID = leader.uniqueId

    fun isLeader(player: Player) = leader == player.uniqueId

}