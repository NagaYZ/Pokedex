package fr.uge.pokedex.data

import android.content.Context

class PokemonRepository {
    private var pokemon : Map<Long, Pokemon> = emptyMap()

    fun loadData(context : Context) {
        println("opening file")
        val reader = context.assets.open("csv/pokemon.csv").bufferedReader()
        val header = reader.readLine()
        pokemon = reader.lineSequence().toList().slice(0 until MAX_POKEMON)
            .filter { it.isNotBlank() }
            .map {
                val (id, identifier, _) = it.split(',')
                Pokemon(id.toLong(), identifier, Pair(Type.UNKNOWN, Type.UNKNOWN))
            }.associateBy { it.id }
        println("done")
    }

    fun getPokemon(id : Long) : Pokemon? {
        return pokemon[id]
    }

    fun getAllPokemon(): Collection<Pokemon> {
        return pokemon.values
    }

    companion object {
        const val MAX_POKEMON : Int = 219
    }
}

