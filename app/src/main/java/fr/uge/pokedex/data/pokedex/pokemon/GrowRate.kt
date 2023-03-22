package fr.uge.pokedex.data.pokedex.pokemon

enum class GrowRate {
    SLOW,
    MEDIUM,
    FAST,
    MEDIUM_SLOW,
    SLOW_THEN_VERY_FAST,
    FAST_THEN_VERY_SLOW;

    override fun toString(): String {
        return when (this) {
            SLOW -> "Slow"
            MEDIUM -> "Medium"
            FAST -> "Fast"
            MEDIUM_SLOW -> "Medium Slow"
            SLOW_THEN_VERY_FAST -> "Slow Then Very Fast"
            FAST_THEN_VERY_SLOW -> "Fast Then Very Slow"
        }
    }
}