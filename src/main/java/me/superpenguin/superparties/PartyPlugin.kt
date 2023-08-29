package me.superpenguin.superparties

import net.kyori.adventure.platform.bukkit.BukkitAudiences

data class PartyPlugin(
    val plugin: SuperParties,
    val audience: BukkitAudiences,
    val partyCommand: PartyCommand
) {

}