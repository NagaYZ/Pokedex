package fr.uge.pokedex.utils.parser

import android.content.Context
import fr.uge.pokedex.data.pokedex.ability.Ability
import fr.uge.pokedex.data.pokedex.pokemon.Generation

class AbilityParser(override val context: Context) : Parser<Ability> {
    override fun loadData(): Map<Long, Ability> {
        val abilities = mutableMapOf<Long, Ability>()

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

        return abilities
    }
}