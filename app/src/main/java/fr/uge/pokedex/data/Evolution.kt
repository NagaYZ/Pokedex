package fr.uge.pokedex.data

import java.util.*

data class Evolution(
    val species: Pokemon,
    val evolvedSpecies: Pokemon,
    var evolutionTrigger: EvolutionTrigger = EvolutionTrigger.UNDEFINED,
    var minimumLevel: Int? = null // Only relevant for evolutions triggered by level up
) {
    override fun hashCode(): Int {
        return Objects.hash(species.id, evolvedSpecies.id)
    }
}
