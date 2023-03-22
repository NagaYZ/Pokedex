package fr.uge.pokedex.utils.parser

import android.content.Context
import fr.uge.pokedex.data.pokedex.evolution.Evolution
import fr.uge.pokedex.data.pokedex.evolution.EvolutionChain
import fr.uge.pokedex.data.pokedex.evolution.EvolutionTrigger

class EvolutionChainParser(override val context: Context) : Parser<EvolutionChain> {
    private val maxPokemonId: Long = 649

    override fun loadData(): Map<Long, EvolutionChain> {
        val evolutionChains = mutableMapOf<Long, EvolutionChain>()
        val evolvesFrom = mutableMapOf<Long, Evolution>()

        // First set up the lineage
        parseLines("csv/core/pokemon_species.csv") { row ->
            val id = row["id"]?.toLong()!!
            val evolvesFromSpeciesId = row["evolves_from_species_id"]!!
            val evolutionChainId = row["evolution_chain_id"]!!

            if(id.toInt() <= maxPokemonId) {
                if(evolutionChainId.isNotBlank()) {
                    val evolutionChain = evolutionChains
                        .getOrPut(evolutionChainId.toLong()) { EvolutionChain(evolutionChainId.toLong()) }

                    if (evolvesFromSpeciesId.isNotBlank()) {
                        val evolution = Evolution(
                            speciesId = id,
                            evolvesFromSpeciesId = evolvesFromSpeciesId.toLong()
                        )
                        evolvesFrom[id] = evolution
                        evolutionChain.evolutions.add(evolution)
                    }
                }
            }
        }

        // Then add the evolution triggers
        parseLines("csv/core/pokemon_evolution.csv") { row ->
            val evolvedSpeciesId = row["evolved_species_id"]?.toLong()!!
            val evolutionTrigger =
                EvolutionTrigger.values()[row["evolution_trigger_id"]?.toInt()!! - 1]
            val minimumLevel = row["minimum_level"]!!
            evolvesFrom[evolvedSpeciesId]?.evolutionTrigger = evolutionTrigger
            if (evolutionTrigger == EvolutionTrigger.LEVEL_UP && minimumLevel.isNotBlank()) {
                evolvesFrom[evolvedSpeciesId]?.minimumLevel = minimumLevel.toInt()
            }
        }

        return evolutionChains
    }
}