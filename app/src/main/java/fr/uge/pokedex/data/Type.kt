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

    companion object {
        fun getType(id : Int): Type {
            return Type.values()[id - 1]
        }
    }
}