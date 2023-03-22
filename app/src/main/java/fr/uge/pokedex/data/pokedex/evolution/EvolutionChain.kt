package fr.uge.pokedex.data.pokedex.evolution

data class EvolutionChain(
    val id: Long,
    val evolutions: MutableList<Evolution> = mutableListOf()
)
