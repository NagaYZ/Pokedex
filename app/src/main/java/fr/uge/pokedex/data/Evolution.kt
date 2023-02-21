package fr.uge.pokedex.data

data class Evolution(
    val speciesId: Int,
    val evolutionTrigger: EvolutionTrigger,
    val level: Int? // Only relevant for evolutions triggered by level up
)

enum class EvolutionTrigger() {
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
    RECOIL_DAMAGE
}
