package fr.uge.pokedex.data.pokedex

data class Ability(
    var id: Long = 0,
    var identifier: String = "",
    var name: String = "",
    var flavorText: String = "",
    var generation: Generation = Generation.GENERATION_I
)
