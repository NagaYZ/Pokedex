package fr.uge.pokedex.data

import android.content.Context

class PokemonRepository {
    private var pokemon : Map<Long, Pokemon> = emptyMap()

    // Pour déstructurer des liste à plus de 5 éléments
    operator fun List<Any>.component6() = this[5]

    fun loadData(context : Context) {
        pokemon = getPokemonFromAssets(context).associateBy { it.id }
        setPokemonType(context)
    }

    private fun getPokemonFromAssets(context : Context): List<Pokemon> {
        val reader = context.assets.open("csv/pokemon.csv").bufferedReader()
        val header = reader.readLine()
        return reader.lineSequence().toList().slice(0 until MAX_POKEMON_ID)
            .filter { it.isNotBlank() }
            .map {
                val (id, identifier, _, height, weight, _) = it.split(',')
                Pokemon(id.toLong(), identifier, Pair(Type.UNKNOWN, Type.UNKNOWN), height.toInt(), weight.toInt())
            }.toList()
    }

    private fun setPokemonType(context : Context) {
        val reader = context.assets.open("csv/pokemon_types.csv").bufferedReader()
        val header = reader.readLine()

        // Pour simuler un break dans une instruction foreach
        // https://stackoverflow.com/questions/32540947/break-and-continue-in-foreach-in-kotlin
        run typing@ {
            reader.lineSequence().toList()
                .filter { it.isNotBlank() }
                .forEach {
                    val (pokemonId, typeId, slot) = it.split(',')
                    if(pokemonId.toInt() > MAX_POKEMON_ID) return@typing

                    val type = Type.getType(typeId.toInt())
                    val currentPokemon = pokemon[pokemonId.toLong()]
                    if (currentPokemon != null) {
                        currentPokemon.type = Pair(
                            if(slot == "1") type else currentPokemon.type.first,
                            if(slot == "2") type else currentPokemon.type.second
                        )
                    }
                }
        }
    }

    fun getPokemon(id : Long) : Pokemon? {
        return pokemon[id]
    }

    fun getAllPokemon(): Collection<Pokemon> {
        return pokemon.values
    }

    companion object {
        const val MAX_POKEMON_ID : Int = 649
    }
}

