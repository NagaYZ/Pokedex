package fr.uge.pokedex.data.pokedex

data class EvolutionChain(
    val evolutions: MutableList<Evolution> = mutableListOf()
)
