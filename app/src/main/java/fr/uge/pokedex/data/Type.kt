package fr.uge.pokedex.data

enum class Type {
    NORMAL,
    FIGHTING,
    FLYING,
    POISON,
    GROUND,
    ROCK,
    BUG,
    GHOST,
    STEEL,
    FIRE,
    WATER,
    GRASS,
    ELECTRIC,
    PSYCHIC,
    ICE,
    DRAGON,
    DARK,
    FAIRY,
    UNKNOWN,
    SHADOW,
    NONE;

    val isImmuneFrom: Set<Type>
        get() = when (this) {
            NORMAL -> setOf(GHOST)
            GHOST -> setOf(NORMAL, FIGHTING)
            FLYING -> setOf(GROUND)
            GROUND -> setOf(ELECTRIC)
            DARK -> setOf(PSYCHIC)
            FAIRY -> setOf(DRAGON)
            STEEL -> setOf(POISON)
            else -> emptySet()
        }

    val isVulnerableTo: Set<Type>
        get() = when (this) {
            NORMAL -> setOf(FIGHTING)
            FIGHTING -> setOf(FLYING, PSYCHIC)
            FLYING -> setOf(ELECTRIC, ICE, ROCK)
            POISON -> setOf(GROUND, PSYCHIC)
            GROUND -> setOf(WATER, GRASS, ICE)
            ROCK -> setOf(WATER, GRASS, FIGHTING, GROUND)
            BUG -> setOf(FIRE, FLYING, ROCK)
            GHOST -> setOf(GHOST, DARK)
            STEEL -> setOf(FIRE, FIGHTING, GROUND)
            FIRE -> setOf(WATER, ROCK, GROUND)
            WATER -> setOf(ELECTRIC, GRASS)
            GRASS -> setOf(FIRE, ICE, FLYING, POISON, BUG)
            ELECTRIC -> setOf(GROUND)
            PSYCHIC -> setOf(BUG, GHOST, DARK)
            ICE -> setOf(FIGHTING, FIRE, STEEL, ROCK)
            DRAGON -> setOf(DRAGON, ICE, FAIRY)
            DARK -> setOf(FIGHTING, BUG, FAIRY)
            FAIRY -> setOf(POISON, STEEL)
            else -> emptySet()
        }

    val isResistantTo: Set<Type>
        get() = when (this) {
            NORMAL -> emptySet()
            FIGHTING -> setOf(BUG, ROCK, DARK)
            FLYING -> setOf(GRASS, FIGHTING, BUG)
            POISON -> setOf(GRASS, FIGHTING, POISON, BUG)
            GROUND -> setOf(POISON, ROCK)
            ROCK -> setOf(NORMAL, FIRE, POISON, FLYING)
            BUG -> setOf(GRASS, FIGHTING, GROUND)
            GHOST -> setOf(POISON, BUG)
            STEEL -> setOf(NORMAL, GRASS, ICE, STEEL, FAIRY, PSYCHIC, FLYING, ROCK, BUG, DRAGON, ELECTRIC, ICE)
            FIRE -> setOf(FIRE, GRASS, FAIRY, STEEL, ICE)
            WATER -> setOf(FIRE, WATER, STEEL, ICE)
            GRASS -> setOf(WATER, ELECTRIC, GRASS, GROUND)
            ELECTRIC -> setOf(ELECTRIC, FLYING, STEEL)
            PSYCHIC -> setOf(FIGHTING, PSYCHIC)
            ICE -> setOf(ICE)
            DRAGON -> setOf(ELECTRIC, FIRE, WATER, GRASS)
            DARK -> setOf(GHOST, DARK)
            FAIRY -> setOf(FIGHTING, BUG, DARK)
            else -> emptySet()
        }
}