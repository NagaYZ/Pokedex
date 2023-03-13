package fr.uge.pokedex.data.pokedex

import java.util.*

enum class EggGroup {
    MONSTER,
    WATER1,
    BUG,
    FLYING,
    GROUND,
    FAIRY,
    PLANT,
    HUMAN_SHAPE,
    WATER3,
    MINERAL,
    INDETERMINATE,
    WATER2,
    DITTO,
    DRAGON,
    NO_EGGS;

    override fun toString(): String {
        return when (this) {
            NO_EGGS -> "No Eggs"
            WATER1 -> "Water 1"
            WATER2 -> "Water 2"
            WATER3 -> "Water 3"
            HUMAN_SHAPE -> "Human Shape"
            else -> super.toString().lowercase(Locale.getDefault())
                .replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }
        }
    }
}