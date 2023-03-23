package fr.uge.pokedex.data.pokedex.pokemon

import android.content.Context
import com.github.doyaaaaaken.kotlincsv.dsl.csvReader
import fr.uge.pokedex.data.pokedex.Parser

class PokemonParser(override val context: Context) : Parser<Pokemon> {
    private val maxPokemonId: Int = 650

    // Custom operator to allow destructuring of list containing n elements
    operator fun List<String>.component6() = this[5]

    override fun loadData(): Map<Long, Pokemon> {
        val pokemon = getPokemonFromAssets().associateBy { it.id }
        setPokemonType(pokemon)
        setPokemonNameAndGenus(pokemon)
        setPokemonEncounters(pokemon)
        setPokemonEvolutions(pokemon)
        setPokemonEggGroups(pokemon)
        setPokemonBaseStats(pokemon)
        setPokemonFlavorText(pokemon)
        setPokemonAbilities(pokemon)
        setPokemonMoves(pokemon)
        return pokemon.filterKeys { it <= maxPokemonId } // Remove pokemon alt form and limit data to fifth generation
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
        parseLines("csv/core/encounters.csv") { row ->
            val version = Version.values()[row["version_id"]?.toInt()!! - 1]
            val locationAreaId = row["location_area_id"]?.toLong()!!
            val pokemonId = row["pokemon_id"]?.toLong()!!
            val encounter = Encounter(locationAreaId, version)
            pokemon[pokemonId]?.encounters?.add(encounter)
        }
    }

    private fun setPokemonEvolutions(pokemon: Map<Long, Pokemon>) {
        parseLines("csv/core/pokemon_species.csv") { row ->
            val id = row["id"]?.toLong()!!
            val captureRate = row["capture_rate"]!!
            val baseHappiness = row["base_happiness"]!!
            val hatchCounter = row["hatch_counter"]!!
            val growthRateId = row["growth_rate_id"]!!
            val evolutionChainId = row["evolution_chain_id"]!!

            if(id.toInt() <= maxPokemonId) {
                pokemon[id]?.captureRate = captureRate.toInt()
                pokemon[id]?.baseHappiness = baseHappiness.toIntOrNull() ?: 0
                pokemon[id]?.hatchCounter = hatchCounter.toIntOrNull() ?: 0
                pokemon[id]?.growRate = GrowRate.values()[growthRateId.toInt() - 1]
                if (evolutionChainId.isNotBlank()) {
                    pokemon[id]?.evolutionChainId = evolutionChainId.toLong()
                }
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
        parseLines("csv/core/pokemon_abilities.csv") { row ->
            val pokemonId = row["pokemon_id"]?.toLong()!!
            val abilityId = row["ability_id"]?.toLong()!!
            when(row["slot"]?.toInt()!!) {
                1 -> pokemon[pokemonId]?.abilities?.idFirst = abilityId
                2 -> pokemon[pokemonId]?.abilities?.idSecond = abilityId
                3 -> pokemon[pokemonId]?.abilities?.hiddenId = abilityId
            }
        }
    }

    private fun setPokemonMoves(pokemon: Map<Long, Pokemon>) {
        parseLines("csv/core/pokemon_moves.csv") { row ->
            val pokemonId = row["pokemon_id"]?.toLong()!!
            val moveId = row["move_id"]?.toLong()!!
            val moveLearnMethod = MoveLearnMethod.values()[row["pokemon_move_method_id"]?.toInt()!! - 1]
            val level = row["level"]?.toInt()!!

            val learnableMove = LearnableMove(
                moveId = moveId,
                level = level,
                method = moveLearnMethod
            )
            pokemon[pokemonId]?.learnSet?.add(learnableMove)
        }
    }
}