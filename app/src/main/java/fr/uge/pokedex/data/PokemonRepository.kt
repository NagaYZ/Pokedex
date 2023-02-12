package fr.uge.pokedex.data

import android.content.Context

class PokemonRepository(
    val context: Context,
    val dataLanguage: Language = Language.ENGLISH, // Language for name, genus and description
    val maxGeneration: Generation = Generation.GENERATION_VII
) {
    lateinit var pokemon: Map<Long, Pokemon>
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
            return reader.lineSequence().toList().slice(0 until maxGeneration.maxId)
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
                    if (pokemonId.toInt() > maxGeneration.maxId) return

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
                if (speciesId.toInt() > maxGeneration.maxId) break

                // Parsing a multiline value, specific to this file
                val descSb = StringBuilder(descFirstLine)
                while (descSb.last() != '"') {
                    descSb.append(" ").append(lines.next())
                }

                if (Language.getLanguage(languageId.toInt()) != dataLanguage) continue
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
                    if (speciesId.toInt() <= maxGeneration.maxId &&
                        Language.getLanguage(languageId.toInt()) == dataLanguage
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
}

