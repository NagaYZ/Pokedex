package fr.uge.pokedex.data.pokedex.evolution

import android.content.Context
import fr.uge.pokedex.data.pokedex.Repository

class EvolutionChainRepository(
    context: Context
) : Repository<EvolutionChain> {
    override lateinit var data: Map<Long, EvolutionChain>
    private val parser = EvolutionChainParser(context)

    init {
        data = parser.loadData()
    }
}

