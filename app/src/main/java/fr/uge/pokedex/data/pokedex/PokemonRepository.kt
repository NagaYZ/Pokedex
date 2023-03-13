package fr.uge.pokedex.data.pokedex

import android.content.Context
import fr.uge.pokedex.utils.DataParser

class PokemonRepository(
    context: Context
) {
    var data: Map<Long, Pokemon>
    private val parser = DataParser(context)

    init {
        data = parser.loadData()
    }

    fun get(id: Long): Pokemon? {
        return data[id]
    }

    fun getAll(): Collection<Pokemon> {
        return data.values
    }
}

