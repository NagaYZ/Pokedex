package fr.uge.pokedex.data.pokedex.move

import android.content.Context
import fr.uge.pokedex.data.pokedex.pokemon.DamageClass
import fr.uge.pokedex.data.pokedex.pokemon.Generation
import fr.uge.pokedex.data.pokedex.pokemon.Type
import fr.uge.pokedex.data.pokedex.pokemon.VersionGroup
import fr.uge.pokedex.data.pokedex.Parser

class MoveParser(override val context: Context) : Parser<Move> {
    override fun loadData(): Map<Long, Move> {
        val moves = mutableMapOf<Long, Move>()

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

        return moves
    }
}