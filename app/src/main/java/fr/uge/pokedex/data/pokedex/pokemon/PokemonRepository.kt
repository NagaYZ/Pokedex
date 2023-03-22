package fr.uge.pokedex.data.pokedex.pokemon

import android.content.Context
import fr.uge.pokedex.data.pokedex.Repository
import fr.uge.pokedex.utils.parser.PokemonParser

class PokemonRepository(
    context: Context
) : Repository<Pokemon> {
    override lateinit var data: Map<Long, Pokemon>
    private val parser = PokemonParser(context)

    init {
        data = parser.loadData()
    }
}

