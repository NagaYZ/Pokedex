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
            val pokemonBuilderMap = getPokemonBuilderFromAssets().associateBy { it.id }
            setPokemonType(pokemonBuilderMap)
            setPokemonDescription(pokemonBuilderMap)
            setPokemonNameAndGenus(pokemonBuilderMap)
            pokemon = pokemonBuilderMap.values.map { it.build() }.associateBy { it.id }
        }

        private fun getPokemonBuilderFromAssets(): List<Pokemon.Builder> {
            val reader = context.assets.open("csv/pokemon.csv").bufferedReader()
            val header = reader.readLine()
            return reader.lineSequence().toList().slice(0 until Companion.MAX_POKEMON_ID)
                .filter { it.isNotBlank() }
                .map {
                    val (id, identifier, _, height, weight, _) = it.split(',')
                    Pokemon.Builder()
                        .id(id.toLong())
                        .identifier(identifier)
                        .height(height.toInt())
                        .weight(weight.toInt())
                }.toList()
        }

        private fun setPokemonType(pokemonBuilderMap: Map<Long?, Pokemon.Builder>) {
            val reader = context.assets.open("csv/pokemon_types.csv").bufferedReader()
            val header = reader.readLine()

            reader.lineSequence().toList()
                .filter { it.isNotBlank() }
                .forEach {
                    val (pokemonId, typeId, slot) = it.split(',')
                    if (pokemonId.toInt() > Companion.MAX_POKEMON_ID) return

                    val type = Type.getType(typeId.toInt())
                    when (slot.toInt()) {
                        1 -> pokemonBuilderMap[pokemonId.toLong()]?.firstType(type)
                        2 -> pokemonBuilderMap[pokemonId.toLong()]?.secondType(type)
                    }
                }
        }

        private fun setPokemonDescription(pokemonBuilderMap: Map<Long?, Pokemon.Builder>) {
            val reader = context.assets.open("csv/pokemon_species_flavor_text.csv").bufferedReader()
            val header = reader.readLine()

            val lines = reader.lineSequence().iterator()
            while (lines.hasNext()) {
                val (speciesId, versionId, languageId, descFirstLine) = lines.next()
                    .split(",", limit = 4)
                if (speciesId.toInt() > Companion.MAX_POKEMON_ID) break

                // Parsing a multiline value, specific to this file
                val descSb = StringBuilder(descFirstLine)
                while (descSb.last() != '"') {
                    descSb.append(" ").append(lines.next())
                }

                if (languageId.toInt() != Companion.LANGUAGE_ID) continue
                val description = descSb.toString()
                    .removeSurrounding("\"")

                pokemonBuilderMap[speciesId.toLong()]?.description(description)
            }
        }

        private fun setPokemonNameAndGenus(pokemonBuilderMap: Map<Long?, Pokemon.Builder>) {
            val reader = context.assets.open("csv/pokemon_species_names.csv").bufferedReader()
            val header = reader.readLine()

            reader.lineSequence().toList()
                .filter { it.isNotBlank() }
                .forEach {
                    val (speciesId, languageId, name, genus) = it.split(',')
                    if (speciesId.toInt() <= Companion.MAX_POKEMON_ID &&
                        languageId.toInt() == LANGUAGE_ID
                    ) {
                        pokemonBuilderMap[speciesId.toLong()]?.name(name)?.genus(genus)
                    }
                }
        }

        private fun setPokemonLocation(pokemonBuilderMap: Map<Long?, Pokemon.Builder>) {
            //TODO
        }

        private fun setPokemonEvolutions(pokemonBuilderMap: Map<Long?, Pokemon.Builder>) {
            //TODO
        }
    }

    companion object {
        const val MAX_POKEMON_ID: Int = 649 // Gen 1 to 5
        const val LANGUAGE_ID = 9 // English
    }
}

