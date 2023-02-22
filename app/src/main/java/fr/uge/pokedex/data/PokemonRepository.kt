package fr.uge.pokedex.data

import android.content.Context

class PokemonRepository(
    val context: Context,
    val languageData: Language = Language.ENGLISH, // For names, descriptions, genus and locations
    val maxGeneration: Generation = Generation.GENERATION_VII
) {
    var data: Map<Long, Pokemon>
    private val parser = PokemonParser()

    init {
        data = parser.loadData()
    }

    fun get(id: Long) : Pokemon? {
        return data[id]
    }

    fun getAll() : Collection<Pokemon> {
        return data.values
    }

    @Suppress("UNUSED_VARIABLE")
    inner class PokemonParser {

        // Custom operator to allow destructuring of list containing 5 elements
        operator fun List<Any>.component6() = this[5]

        fun loadData(): Map<Long, Pokemon> {
            val pokemon = getPokemonFromAssets().associateBy { it.id }
            setPokemonType(pokemon)
            setPokemonDescription(pokemon)
            setPokemonNameAndGenus(pokemon)
            setPokemonLocation(pokemon)
            setPokemonEvolutions(pokemon)
            return pokemon
        }

        private fun parseLines(filename: String, action: (String) -> Unit) {
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

        private fun setPokemonType(pokemon: Map<Long, Pokemon>) {
            parseLines("csv/pokemon_types.csv") { line ->
                val (pokemonId, typeId, slot) = line.split(',')
                if (pokemonId.toInt() > maxGeneration.maxId) return@parseLines

                val type = Type.values()[typeId.toInt() - 1]
                val currentPokemon = pokemon[pokemonId.toLong()]
                when (slot.toInt()) {
                    1 -> currentPokemon?.type = currentPokemon?.type?.copy(first = type)!!
                    2 -> currentPokemon?.type = currentPokemon?.type?.copy(second = type)!!
                }
            }
        }

        private fun setPokemonDescription(pokemon: Map<Long, Pokemon>) {
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

                if (Language.values()[languageId.toInt() - 1] != languageData) continue
                val description = descSb.toString()
                    .removeSurrounding("\"")

                pokemon[speciesId.toLong()]?.description = description
            }
        }

        private fun setPokemonNameAndGenus(pokemon: Map<Long, Pokemon>) {
            parseLines("csv/pokemon_species_names.csv") { line ->
                val (speciesId, languageId, name, genus) = line.split(',')
                if (speciesId.toInt() <= maxGeneration.maxId &&
                    Language.values()[languageId.toInt() - 1] == languageData
                ) {
                    pokemon[speciesId.toLong()]?.name = name
                    pokemon[speciesId.toLong()]?.genus = genus
                }
            }
        }

        private fun setPokemonLocation(pokemon: Map<Long, Pokemon>) {
            val locations = HashMap<Long, Location>()

            parseLines("csv/locations.csv") { line ->
                val (id, regionId, _) = line.split(',')
                val region = if(regionId.isBlank()) Region.UNKNOWN else Region.values()[regionId.toInt() - 1]
                locations[id.toLong()] = Location(id.toLong(), region = region)
            }

            parseLines("csv/location_names.csv") { line ->
                val (locationId, localLanguageId, name, subtitle) = line.split(',')
                if(languageData == Language.values()[localLanguageId.toInt() - 1]) {
                    locations[locationId.toLong()]?.name = name
                }
            }

            parseLines("csv/location_areas.csv") { line ->
                val (id, locationId, gameIndex, identifier) = line.split(',')
                locations[locationId.toLong()]?.area = Area(id.toLong())
            }

            val locationsByAreaId = locations.values.associateBy { it.area?.id }

            parseLines("csv/location_area_prose.csv") { line ->
                val (locationAreaId, localLanguageId, name) = line.split(',')
                locationsByAreaId[locationAreaId.toLong()]?.name = name
            }

            parseLines("csv/encounters.csv") { line ->
                val (id, versionId, locationAreaId, encounterSlotId, pokemonId, _) = line.split(',')
                val location = locationsByAreaId[locationAreaId.toLong()]
                if(location != null) {
                    pokemon[pokemonId.toLong()]?.locations?.add(location)
                }
            }
        }

        private fun setPokemonEvolutions(pokemon: Map<Long, Pokemon>) {
            // First set up the lineage
            parseLines("csv/pokemon_species.csv") { line ->
                val (id, identifier, generationId, evolvesFromSpeciesId, _) = line.split(',')
                if (id.toInt() <= maxGeneration.maxId && evolvesFromSpeciesId.isNotBlank()) {
                    val evolution = Evolution(evolvesFromSpeciesId.toInt(), id.toInt())
                    pokemon[id.toLong()]?.evolvesFrom = evolution
                    pokemon[evolvesFromSpeciesId.toLong()]?.evolvesInto = evolution
                }
            }

            // Then add the evolution triggers
            parseLines("csv/pokemon_evolution.csv") { line ->
                val (id, evolvedSpeciesId, evolutionTriggerId, triggerItemId, minimumLevel, _) = line.split(',')

                if (evolvedSpeciesId.toInt() <= maxGeneration.maxId) {
                    val evolutionTrigger =
                        EvolutionTrigger.values()[evolutionTriggerId.toInt() - 1]
                    pokemon[evolvedSpeciesId.toLong()]?.evolvesFrom?.evolutionTrigger =
                        evolutionTrigger
                    if (evolutionTrigger == EvolutionTrigger.LEVEL_UP && minimumLevel.isNotBlank()) {
                        pokemon[evolvedSpeciesId.toLong()]?.evolvesFrom?.minimumLevel =
                            minimumLevel.toInt()
                    }
                }
            }
        }
    }
}

