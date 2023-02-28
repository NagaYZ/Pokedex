package fr.uge.pokedex.data

data class Evolution(
    val speciesId: Int,
    val evolvedSpeciesId: Int,
    var evolutionTrigger: EvolutionTrigger = EvolutionTrigger.UNDEFINED,
    var minimumLevel: Int? = null // Only relevant for evolutions triggered by level up
)
