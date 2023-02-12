package fr.uge.pokedex.data

import android.content.Context

class PokemonRepository(private val context: Context) {
    private var pokemon: Map<Long, Pokemon> = emptyMap()
    private val parser = CsvParser()

    init {
        parser.loadData()
    }

    fun get(id: Long): Pokemon? {
        return pokemon[id]
    }

    fun getAll(): Collection<Pokemon> {
        return pokemon.values
    }

    inner class CsvParser {

        // Custom operator to allow destructuring of list containing 5 elements
        operator fun List<Any>.component6() = this[5]

        fun loadData() {
            pokemon = getPokemonFromAssets().associateBy { it.id }
            setPokemonType()
            setPokemonDescription()
        }

        private fun getPokemonFromAssets(): List<Pokemon> {
            val reader = context.assets.open("csv/pokemon.csv").bufferedReader()
            val header = reader.readLine()
            return reader.lineSequence().toList().slice(0 until Companion.MAX_POKEMON_ID)
                .filter { it.isNotBlank() }
                .map {
                    val (id, identifier, _, height, weight, _) = it.split(',')
                    Pokemon(
                        id.toLong(),
                        identifier,
                        Pair(Type.UNKNOWN, Type.UNKNOWN),
                        height.toInt(),
                        weight.toInt(),
                        ""
                    )
                }.toList()
        }

        private fun setPokemonType() {
            val reader = context.assets.open("csv/pokemon_types.csv").bufferedReader()
            val header = reader.readLine()

            reader.lineSequence().toList()
                .filter { it.isNotBlank() }
                .forEach {
                    val (pokemonId, typeId, slot) = it.split(',')
                    if (pokemonId.toInt() > Companion.MAX_POKEMON_ID) return

                    val type = Type.getType(typeId.toInt())
                    val currentPokemon = pokemon[pokemonId.toLong()]
                    if (currentPokemon != null) {
                        currentPokemon.type = Pair(
                            if (slot == "1") type else currentPokemon.type.first,
                            if (slot == "2") type else currentPokemon.type.second
                        )
                    }
                }
        }

        private fun setPokemonDescription() {
            val reader = context.assets.open("csv/pokemon_species_flavor_text.csv").bufferedReader()
            val header = reader.readLine()

            val lines = reader.lineSequence().iterator()
            while(lines.hasNext()) {
                val (species_id, version_id, language_id, descFirstLine) = lines.next().split(",", limit = 4)
                if(species_id.toInt() > Companion.MAX_POKEMON_ID) break

                // Parsing a multiline value, specific to this file
                val descSb = StringBuilder(descFirstLine)
                while(descSb.last() != '"') {
                    descSb.append(" ").append(lines.next())
                }

                if(language_id.toInt() != Companion.LANGUAGE_ID) continue
                val description = descSb.toString()
                    .removeSurrounding("\"")

                pokemon[species_id.toLong()]?.description = description
            }
        }

        private fun setPokemonNameAndGenus() {
            //TODO
        }

        private fun setPokemonLocation() {
            //TODO
        }

        private fun setPokemonEvolutions() {
            //TODO
        }
    }

    companion object {
        const val MAX_POKEMON_ID: Int = 649 // Gen 1 to 5
        const val LANGUAGE_ID = 9 // English
    }
}

