package fr.uge.pokedex.data

import android.content.Context
import com.github.doyaaaaaken.kotlincsv.dsl.csvReader

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

    fun get(id: Long): Pokemon? {
        return data[id]
    }

    fun getAll(): Collection<Pokemon> {
        return data.values
    }

    inner class PokemonParser {

        // Custom operator to allow destructuring of list containing n elements
        operator fun List<String>.component6() = this[5]
        operator fun List<String>.component7() = this[6]
        operator fun List<String>.component8() = this[7]
        operator fun List<String>.component9() = this[8]
        operator fun List<String>.component10() = this[9]
        operator fun List<String>.component11() = this[10]
        operator fun List<String>.component12() = this[11]
        operator fun List<String>.component13() = this[12]
        operator fun List<String>.component14() = this[13]
        operator fun List<String>.component15() = this[14]
        operator fun List<String>.component16() = this[15]

        fun loadData(): Map<Long, Pokemon> {
            val pokemon = getPokemonFromAssets().associateBy { it.id }
            setPokemonType(pokemon)
            setPokemonDescription(pokemon)
            setPokemonNameAndGenus(pokemon)
            setPokemonLocation(pokemon)
            setPokemonEvolutions(pokemon)
            setPokemonEggGroups(pokemon)
            setPokemonBaseStats(pokemon)
            return pokemon
        }

        @Suppress("UNUSED_VARIABLE")
        private fun parseLines(filename: String, action: (String) -> Unit) {
            val reader = context.assets.open(filename).bufferedReader()
            val header = reader.readLine()

            reader.lineSequence().toList().filter { it.isNotBlank() }.forEach(action)
        }

        // Implemented with csv-kotlin lib, used for multiline value or when parsing file with
        // too many columns
        private fun parseLines2(filename: String, action: (Map<String, String>) -> Unit) {
            csvReader().open(context.assets.open(filename)) {
                    readAllWithHeaderAsSequence().forEach(action)
                }
        }

        @Suppress("UNUSED_VARIABLE")
        private fun getPokemonFromAssets(): List<Pokemon> {
            val reader = context.assets.open("csv/pokemon.csv").bufferedReader()
            val header = reader.readLine()

            return reader.lineSequence().toList().slice(0 until maxGeneration.maxId)
                .filter { it.isNotBlank() }.map {
                    val (id, identifier, _, height, weight, baseExperience) = it.split(',')
                    Pokemon(
                        id = id.toLong(),
                        identifier = identifier,
                        height = height.toInt(),
                        weight = weight.toInt(),
                        baseExperience = baseExperience.toInt()
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
            parseLines2("csv/pokemon_species_flavor_text.csv") { row ->
                val speciesId = row["species_id"]!!
                val versionId = row["version_id"]!!
                val languageId = row["language_id"]!!
                val description = row["flavor_text"]?.removeSurrounding("\"")!!

                if (Language.values()[languageId.toInt() - 1] == languageData) {
                    val version = Version.values()[versionId.toInt() - 1]
                    pokemon[speciesId.toLong()]?.descriptions?.set(version, description)
                }
            }
        }


        private fun setPokemonNameAndGenus(pokemon: Map<Long, Pokemon>) {
            parseLines("csv/pokemon_species_names.csv") { line ->
                val (speciesId, languageId, name, genus) = line.split(',')
                if (speciesId.toInt() <= maxGeneration.maxId && Language.values()[languageId.toInt() - 1] == languageData) {
                    pokemon[speciesId.toLong()]?.name = name
                    pokemon[speciesId.toLong()]?.genus = genus
                }
            }
        }

        private fun setPokemonLocation(pokemon: Map<Long, Pokemon>) {
            val locations = HashMap<Long, Location>()

            parseLines("csv/locations.csv") { line ->
                val (id, regionId) = line.split(',')
                val region =
                    if (regionId.isBlank()) Region.UNKNOWN else Region.values()[regionId.toInt() - 1]
                locations[id.toLong()] = Location(id.toLong(), region = region)
            }

            parseLines("csv/location_names.csv") { line ->
                val (locationId, localLanguageId, name) = line.split(',')
                if (languageData == Language.values()[localLanguageId.toInt() - 1]) {
                    locations[locationId.toLong()]?.name = name
                }
            }

            parseLines("csv/location_areas.csv") { line ->
                val (id, locationId) = line.split(',')
                locations[locationId.toLong()]?.area = Area(id.toLong())
            }

            val locationsByAreaId = locations.values.associateBy { it.area?.id }

            parseLines("csv/location_area_prose.csv") { line ->
                val (locationAreaId, _, name) = line.split(',')
                locationsByAreaId[locationAreaId.toLong()]?.area?.name = name
            }

            parseLines("csv/encounters.csv") { line ->
                val (_, versionId, locationAreaId, _, pokemonId, _) = line.split(',')
                val location = locationsByAreaId[locationAreaId.toLong()]
                if (location != null) {
                    val version = Version.values()[versionId.toInt() - 1]
                    pokemon[pokemonId.toLong()]?.locations?.getOrPut(version) { mutableSetOf() }
                        ?.add(location)
                }
            }
        }

        private fun setPokemonEvolutions(pokemon: Map<Long, Pokemon>) {
            // First set up the lineage
            parseLines2("csv/pokemon_species.csv") { row ->
                val id = row["id"]!!
                val evolvesFromSpeciesId = row["evolves_from_species_id"]!!
                val captureRate = row["capture_rate"]!!
                val baseHappiness = row["base_happiness"]!!
                val hatchCounter = row["hatch_counter"]!!
                val growthRateId = row["growth_rate_id"]!!

                if (id.toInt() <= maxGeneration.maxId) {
                    if (evolvesFromSpeciesId.isNotBlank()) {
                        val evolution = Evolution(evolvesFromSpeciesId.toInt(), id.toInt())
                        pokemon[id.toLong()]?.evolvesFrom = evolution
                        pokemon[evolvesFromSpeciesId.toLong()]?.evolvesInto?.add(evolution)
                    }

                    pokemon[id.toLong()]?.captureRate = captureRate.toInt()
                    pokemon[id.toLong()]?.baseHappiness = baseHappiness.toInt()
                    pokemon[id.toLong()]?.hatchCounter = hatchCounter.toInt()
                    pokemon[id.toLong()]?.growRate = GrowRate.values()[growthRateId.toInt() - 1]
                }
            }

            // Then add the evolution triggers
            parseLines("csv/pokemon_evolution.csv") { line ->
                val (_, evolvedSpeciesId, evolutionTriggerId, _, minimumLevel) = line.split(',')

                if (evolvedSpeciesId.toInt() <= maxGeneration.maxId) {
                    val evolutionTrigger = EvolutionTrigger.values()[evolutionTriggerId.toInt() - 1]
                    pokemon[evolvedSpeciesId.toLong()]?.evolvesFrom?.evolutionTrigger =
                        evolutionTrigger
                    if (evolutionTrigger == EvolutionTrigger.LEVEL_UP && minimumLevel.isNotBlank()) {
                        pokemon[evolvedSpeciesId.toLong()]?.evolvesFrom?.minimumLevel =
                            minimumLevel.toInt()
                    }
                }
            }
        }

        private fun setPokemonEggGroups(pokemon: Map<Long, Pokemon>) {
            parseLines("csv/pokemon_egg_groups.csv") { line ->
                val (speciesId, eggGroupId) = line.split(',')
                val eggGroup = EggGroup.values()[eggGroupId.toInt() - 1]
                pokemon[speciesId.toLong()]?.eggGroups?.add(eggGroup)
            }
        }

        private fun setPokemonBaseStats(pokemon: Map<Long, Pokemon>) {
            parseLines("csv/pokemon_stats.csv") { line ->
                val (pokemonId, statId, baseStat) = line.split(',')
                when (statId.toInt()) {
                    1 -> pokemon[pokemonId.toLong()]?.baseStats?.hp = baseStat.toInt()
                    2 -> pokemon[pokemonId.toLong()]?.baseStats?.attack = baseStat.toInt()
                    3 -> pokemon[pokemonId.toLong()]?.baseStats?.defense = baseStat.toInt()
                    4 -> pokemon[pokemonId.toLong()]?.baseStats?.specialAttack = baseStat.toInt()
                    5 -> pokemon[pokemonId.toLong()]?.baseStats?.specialDefense = baseStat.toInt()
                    6 -> pokemon[pokemonId.toLong()]?.baseStats?.speed = baseStat.toInt()
                }
            }
        }
    }
}

