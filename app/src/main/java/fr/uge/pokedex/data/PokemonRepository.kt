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

        private fun parseCSVLines(filename: String, action: (String) -> Unit) {
            val reader = context.assets.open(filename).bufferedReader()
            val header = reader.readLine()

            reader.lineSequence().toList()
                .filter { it.isNotBlank() }
                .forEach(action)
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
            parseCSVLines("csv/pokemon_types.csv") { line ->
                val (pokemonId, typeId, slot) = line.split(',')
                if (pokemonId.toInt() > maxGeneration.maxId) return@parseCSVLines

                val type = Type.getType(typeId.toInt())
                val currentPokemon = pokemon[pokemonId.toLong()]
                when (slot.toInt()) {
                    1 -> currentPokemon?.type = currentPokemon?.type?.copy(first = type)!!
                    2 -> currentPokemon?.type = currentPokemon?.type?.copy(second = type)!!
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
            parseCSVLines("csv/pokemon_species_names.csv") { line ->
                val (speciesId, languageId, name, genus) = line.split(',')
                if (speciesId.toInt() <= maxGeneration.maxId &&
                    Language.getLanguage(languageId.toInt()) == dataLanguage
                ) {
                    pokemon[speciesId.toLong()]?.name = name
                    pokemon[speciesId.toLong()]?.genus = genus
                }
            }
        }

        private fun setPokemonLocation() {
            parseCSVLines("csv/encounters.csv") { line ->
                //TODO
            }
        }

        private fun setPokemonEvolutions() {
            parseCSVLines("csv/pokemon_evolution.csv") { line ->
                //TODO
            }
        }
    }
}

