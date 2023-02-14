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
            pokemon = getPokemonFromAssets().associateBy { it.id }
            setPokemonType()
            setPokemonDescription()
            setPokemonNameAndGenus()
        }

        private fun getPokemonFromAssets(): List<Pokemon> {
            val reader = context.assets.open("csv/pokemon.csv").bufferedReader()
            val header = reader.readLine()
            return reader.lineSequence().toList().slice(0 until maxGeneration.maxId)
                .filter { it.isNotBlank() }
                .map {
                    val (id, identifier, _, height, weight, _) = it.split(',')
                    Pokemon(
                        id = id.toLong(),
                        identifier = identifier,
                        height = height.toInt(),
                        weight = weight.toInt()
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
                    if (pokemonId.toInt() > maxGeneration.maxId) return

                    val type = Type.getType(typeId.toInt())
                    when (slot.toInt()) {
                        1 -> pokemon[pokemonId.toLong()]?.type?.copy(first = type)
                        2 -> pokemon[pokemonId.toLong()]?.type?.copy(second = type)
                    }
                }
        }

        private fun setPokemonDescription() {
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

                pokemon[speciesId.toLong()]?.description = description
            }
        }

        private fun setPokemonNameAndGenus() {
            val reader = context.assets.open("csv/pokemon_species_names.csv").bufferedReader()
            val header = reader.readLine()

            reader.lineSequence().toList()
                .filter { it.isNotBlank() }
                .forEach {
                    val (speciesId, languageId, name, genus) = it.split(',')
                    if (speciesId.toInt() <= maxGeneration.maxId &&
                        Language.getLanguage(languageId.toInt()) == dataLanguage
                    ) {
                        pokemon[speciesId.toLong()]?.name = name
                        pokemon[speciesId.toLong()]?.genus = genus
                    }
                }
        }

        private fun setPokemonLocation() {
            //TODO
        }

        private fun setPokemonEvolutions() {
            //TODO
        }
    }
}

