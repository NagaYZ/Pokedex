package fr.uge.pokedex.utils

import android.content.Context
import com.github.doyaaaaaken.kotlincsv.dsl.csvReader
import fr.uge.pokedex.data.pokedex.*

class DataParser(private val context: Context) {

    // Custom operator to allow destructuring of list containing n elements
    operator fun List<String>.component6() = this[5]

    fun loadData(): Map<Long, Pokemon> {
        val pokemon = getPokemonFromAssets().associateBy { it.id }
        setPokemonType(pokemon)
        setPokemonNameAndGenus(pokemon)
//        setPokemonEncounters(pokemon)
        setPokemonEvolutions(pokemon)
        setPokemonEggGroups(pokemon)
        setPokemonBaseStats(pokemon)
        setPokemonFlavorText(pokemon)
        setPokemonAbilities(pokemon)
//        setPokemonMoves(pokemon)
        return pokemon.filterKeys { it < 650L } // Remove pokemon alt form and limit data to fifth generation
    }

    private fun parseLines(filename: String, action: (Map<String, String>) -> Unit) {
        csvReader().open(context.assets.open(filename)) {
            readAllWithHeaderAsSequence().forEach(action)
        }
    }

    private fun getPokemonFromAssets(): List<Pokemon> {
        return csvReader().open(context.assets.open("csv/core/pokemon.csv")) {
            return@open readAllWithHeaderAsSequence().map { row ->
                val id = row["id"]?.toLong()!!
                return@map Pokemon(
                    id = id,
                    identifier = row["identifier"]!!,
                    height = row["height"]?.toInt()!!,
                    weight = row["weight"]?.toInt()!!,
                    baseExperience = if (row["base_experience"]?.isNotBlank()!!)
                        row["base_experience"]?.toInt()!!
                    else 0,
                    icon = context.resources.getIdentifier("icon_pkm_$id", "drawable", context.packageName),
                    sprite = context.resources.getIdentifier("pokemon_$id", "drawable", context.packageName),
                )
            }.toList()
        }
    }

    private fun setPokemonType(pokemon: Map<Long, Pokemon>) {
        parseLines("csv/core/pokemon_types.csv") { row ->
            val pokemonId = row["pokemon_id"]?.toLong()!!
            val type = Type.values()[row["type_id"]?.toInt()!! - 1]
            val slot = row["slot"]?.toInt()!!

            val currentPokemon = pokemon[pokemonId]
            when (slot) {
                1 -> currentPokemon?.type = currentPokemon?.type?.copy(first = type)!!
                2 -> currentPokemon?.type = currentPokemon?.type?.copy(second = type)!!
            }
        }
    }

    private fun setPokemonFlavorText(pokemon: Map<Long, Pokemon>) {
        parseLines("csv/core/pokemon_species_flavor_text.csv") { row ->
            val speciesId = row["species_id"]!!
            val versionId = row["version_id"]!!
            val description = row["flavor_text"]
                ?.removeSurrounding("\"")?.replace("\n", " ")!!
            val version = Version.values()[versionId.toInt() - 1]

            pokemon[speciesId.toLong()]?.pokedexEntries?.put(version, description)
            pokemon[speciesId.toLong()]?.description = description
        }
    }


    private fun setPokemonNameAndGenus(pokemon: Map<Long, Pokemon>) {
        parseLines("csv/core/pokemon_species_names.csv") { row ->
            val speciesId = row["pokemon_species_id"]?.toLong()!!
            val name = row["name"]!!
            val genus = row["genus"]!!
            pokemon[speciesId]?.name = name
            pokemon[speciesId]?.genus = genus
        }
    }

    private fun setPokemonEncounters(pokemon: Map<Long, Pokemon>) {
        val locations = HashMap<Long, Location>()

        parseLines("csv/core/locations.csv") { row ->
            val id = row["id"]?.toLong()!!
            val region = if (row["region_id"]?.isBlank()!!) Region.UNKNOWN
            else Region.values()[row["region_id"]?.toInt()!! - 1]
            locations[id] = Location(id, region = region)
        }

        parseLines("csv/core/location_names.csv") { row ->
            val locationId = row["location_id"]?.toLong()!!
            val name = row["name"]!!
            locations[locationId]?.name = name
        }

        parseLines("csv/core/location_areas.csv") { row ->
            val id = row["id"]?.toLong()!!
            val locationId = row["location_id"]?.toLong()!!
            locations[locationId]?.area = Area(id)
        }

        val locationsByAreaId = locations.values.associateBy { it.area?.id }

        parseLines("csv/core/location_area_prose.csv") { row ->
            val locationAreaId = row["location_area_id"]?.toLong()!!
            val name = row["name"]!!
            locationsByAreaId[locationAreaId]?.area?.name = name
        }

        parseLines("csv/core/encounters.csv") { row ->
            val version = Version.values()[row["version_id"]?.toInt()!! - 1]
            val locationAreaId = row["location_area_id"]?.toLong()!!
            val pokemonId = row["pokemon_id"]?.toLong()!!
            val location = locationsByAreaId[locationAreaId]
            if (location != null) {
                val encounter = Encounter(location, version)
                pokemon[pokemonId]?.encounters?.add(encounter)
            }
        }
    }

    private fun setPokemonEvolutions(pokemon: Map<Long, Pokemon>) {
        val evolvesFrom = mutableMapOf<Long, Evolution>()

        // First set up the lineage
        parseLines("csv/core/pokemon_species.csv") { row ->
            val id = row["id"]!!
            val evolvesFromSpeciesId = row["evolves_from_species_id"]!!
            val captureRate = row["capture_rate"]!!
            val baseHappiness = row["base_happiness"]!!
            val hatchCounter = row["hatch_counter"]!!
            val growthRateId = row["growth_rate_id"]!!

            if (evolvesFromSpeciesId.isNotBlank()) {
                val evolvedSpecies = pokemon[id.toLong()]!!
                val species = pokemon[evolvesFromSpeciesId.toLong()]!!
                val evolution = Evolution(species = species, evolvedSpecies = evolvedSpecies)
                pokemon[id.toLong()]?.evolvesFrom = evolution
                pokemon[evolvesFromSpeciesId.toLong()]?.evolvesInto?.add(evolution)
                evolvesFrom[id.toLong()] = evolution

                pokemon[id.toLong()]?.evolutionChain =
                    pokemon[evolvesFromSpeciesId.toLong()]?.evolutionChain!!
                pokemon[id.toLong()]?.evolutionChain?.evolutions?.add(evolution)
            }

            pokemon[id.toLong()]?.captureRate = captureRate.toInt()
            pokemon[id.toLong()]?.baseHappiness = baseHappiness.toIntOrNull() ?: 0
            pokemon[id.toLong()]?.hatchCounter = hatchCounter.toIntOrNull() ?: 0
            pokemon[id.toLong()]?.growRate = GrowRate.values()[growthRateId.toInt() - 1]
        }

        // Then add the evolution triggers
        parseLines("csv/core/pokemon_evolution.csv") { row ->
            val evolvedSpeciesId = row["evolved_species_id"]?.toLong()!!
            val evolutionTrigger =
                EvolutionTrigger.values()[row["evolution_trigger_id"]?.toInt()!! - 1]
            val minimumLevel = row["minimum_level"]!!
            evolvesFrom[evolvedSpeciesId]?.evolutionTrigger = evolutionTrigger
            if (evolutionTrigger == EvolutionTrigger.LEVEL_UP && minimumLevel.isNotBlank()) {
                evolvesFrom[evolvedSpeciesId]?.minimumLevel = minimumLevel.toInt()
            }
        }
    }

    private fun setPokemonEggGroups(pokemon: Map<Long, Pokemon>) {
        parseLines("csv/core/pokemon_egg_groups.csv") { row ->
            val speciesId = row["species_id"]?.toLong()!!
            val eggGroup = EggGroup.values()[row["egg_group_id"]?.toInt()!! - 1]
            pokemon[speciesId]?.eggGroups?.add(eggGroup)
        }
    }

    private fun setPokemonBaseStats(pokemon: Map<Long, Pokemon>) {
        parseLines("csv/core/pokemon_stats.csv") { row ->
            val pokemonId = row["pokemon_id"]?.toLong()!!
            val stat = Stat.values()[row["stat_id"]?.toInt()!! - 1]
            val baseStat = row["base_stat"]?.toInt()!!
            when (stat) {
                Stat.HP -> pokemon[pokemonId]?.baseStats?.hp = baseStat
                Stat.ATTACK -> pokemon[pokemonId]?.baseStats?.attack = baseStat
                Stat.DEFENSE -> pokemon[pokemonId]?.baseStats?.defense = baseStat
                Stat.SPECIAL_ATTACK -> pokemon[pokemonId]?.baseStats?.specialAttack = baseStat
                Stat.SPECIAL_DEFENSE -> pokemon[pokemonId]?.baseStats?.specialDefense = baseStat
                Stat.SPEED -> pokemon[pokemonId]?.baseStats?.speed = baseStat
            }
        }
    }

    private fun setPokemonAbilities(pokemon: Map<Long, Pokemon>) {
        val abilities = HashMap<Long, Ability>()
        parseLines("csv/core/abilities.csv") { row ->
            val id = row["id"]?.toLong()!!
            val identifier = row["identifier"]!!
            val generation = Generation.values()[row["generation_id"]?.toInt()!! - 1]
            abilities[id] = Ability(id = id, identifier = identifier, generation = generation)
        }

        parseLines("csv/core/ability_names.csv") { row ->
            val id = row["ability_id"]?.toLong()!!
            val name = row["name"]!!
            abilities[id]?.name = name
        }

        parseLines("csv/core/ability_flavor_text.csv") { row ->
            val id = row["ability_id"]?.toLong()!!
            val flavorText = row["flavor_text"]!!
            abilities[id]?.flavorText = flavorText
        }

        parseLines("csv/core/pokemon_abilities.csv") { row ->
            val pokemonId = row["pokemon_id"]?.toLong()!!
            val abilityId = row["ability_id"]?.toLong()!!
            val slot = row["slot"]?.toInt()!!
            val ability = abilities[abilityId]
            when(slot) {
                1 -> pokemon[pokemonId]?.abilities?.first = ability
                2 -> pokemon[pokemonId]?.abilities?.second = ability
                3 -> pokemon[pokemonId]?.abilities?.hidden = ability
            }
        }
    }

    private fun setPokemonMoves(pokemon: Map<Long, Pokemon>) {
        val moves = HashMap<Long, Move>()

        parseLines("csv/core/moves.csv") { row ->
            val id = row["id"]?.toLong()!!

            val identifier = row["identifier"]!!
            val generation = Generation.values()[row["generation_id"]?.toInt()!! - 1]
            val type = when(val typeId = row["type_id"]?.toInt()!!) {
                10001 -> Type.SHADOW
                10002 -> Type.UNKNOWN
                else -> Type.values()[typeId - 1]
            }
            val power = row["power"]?.toIntOrNull() ?: 0
            val pp = row["pp"]?.toIntOrNull() ?: 0
            val priority = row["priority"]?.toIntOrNull() ?: 0
            val damageClass = DamageClass.values()[row["damage_class_id"]?.toInt()!! - 1]
            val accuracy = row["accuracy"]?.toIntOrNull() ?: 0

            moves[id] = Move(
                id = id,
                identifier = identifier,
                generation = generation,
                type = type,
                power = power,
                pp = pp,
                priority = priority,
                damageClass = damageClass,
                accuracy = accuracy
            )
        }

        parseLines("csv/core/move_names.csv") { row ->
            val moveId = row["move_id"]?.toLong()!!
            val name = row["name"]!!
            moves[moveId]?.name = name
        }

        parseLines("csv/core/move_flavor_text.csv") { row ->
            val moveId = row["move_id"]?.toLong()!!
            val versionGroup = VersionGroup.values()[row["version_group_id"]?.toInt()!! - 1]
            val flavorText = row["flavor_text"]?.removeSurrounding("\"")!!
            moves[moveId]?.descriptions?.set(versionGroup, flavorText)
        }

        parseLines("csv/core/pokemon_moves.csv") { row ->
            val pokemonId = row["pokemon_id"]?.toLong()!!
            val moveId = row["move_id"]?.toLong()!!
            val moveLearnMethod = MoveLearnMethod.values()[row["pokemon_move_method_id"]?.toInt()!! - 1]
            val level = row["level"]?.toInt()!!

            val move = moves[moveId]!!
            val learnableMove = LearnableMove(move = move, level = level, method = moveLearnMethod)
            pokemon[pokemonId]?.learnSet?.add(learnableMove)
        }
    }
}