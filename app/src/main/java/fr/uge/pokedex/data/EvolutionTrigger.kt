package fr.uge.pokedex.data

import java.util.*

enum class EvolutionTrigger {
    LEVEL_UP,
    TRADE,
    USE_ITEM,
    SHED,
    SPIN,
    TOWER_OF_DARKNESS,
    TOWER_OF_WATERS,
    THREE_CRITICAL_HITS,
    TAKE_DAMAGE,
    OTHER,
    AGILE_STYLE_MOVE,
    STRONG_STYLE_MOVE,
    RECOIL_DAMAGE,
    UNDEFINED;

    override fun toString(): String {
        return super.toString().replace("_", " ").split(" ")
            .joinToString(" ") { it.lowercase().capitalize(Locale.ROOT) }
    }
}