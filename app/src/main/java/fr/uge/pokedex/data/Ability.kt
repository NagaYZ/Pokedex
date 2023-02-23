package fr.uge.pokedex.data

import java.util.*

data class Ability(
    var id: Long = 0,
    var identifier: String = "",
    var name: String = "",
    val descriptions: MutableMap<VersionGroup, String> = EnumMap(VersionGroup::class.java),
    var generation: Generation = Generation.GENERATION_I
)
