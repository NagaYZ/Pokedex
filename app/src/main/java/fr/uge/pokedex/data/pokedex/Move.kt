package fr.uge.pokedex.data.pokedex

import java.util.EnumMap

data class Move(
    val id: Long,
    var name: String = "",
    val identifier: String,
    val generation: Generation,
    val type: Type,
    val power: Int,
    val pp: Int,
    val accuracy: Int,
    val priority: Int,
    val damageClass: DamageClass,
    var descriptions: MutableMap<VersionGroup, String> = EnumMap(VersionGroup::class.java)
)
