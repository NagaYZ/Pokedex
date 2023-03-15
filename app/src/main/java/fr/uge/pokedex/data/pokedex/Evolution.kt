package fr.uge.pokedex.data.pokedex

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

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Evolution

        if (species.id != other.species.id) return false
        if (evolvedSpecies.id != other.evolvedSpecies.id) return false

        return true
    }

    override fun toString(): String {
        return "Evolution={species=" + species.id + ", evolvedSpecies=" + evolvedSpecies.id + "}"
    }
}
