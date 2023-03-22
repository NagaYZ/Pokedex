package fr.uge.pokedex.data.pokedex.evolution

data class Evolution(
    val speciesId: Long,
    val evolvesFromSpeciesId: Long,
    var evolutionTrigger: EvolutionTrigger = EvolutionTrigger.UNDEFINED,
    var minimumLevel: Int? = null // Only relevant for evolutions triggered by level up
)
