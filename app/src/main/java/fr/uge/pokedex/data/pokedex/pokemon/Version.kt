package fr.uge.pokedex.data.pokedex.pokemon

import java.util.*

enum class Version {
    RED,
    BLUE,
    YELLOW,
    GOLD,
    SILVER,
    CRYSTAL,
    RUBY,
    SAPPHIRE,
    EMERALD,
    FIRERED,
    LEAFGREEN,
    DIAMOND,
    PEARL,
    PLATINUM,
    HEARTGOLD,
    SOULSILVER,
    BLACK,
    WHITE,
    COLOSSEUM,
    XD,
    BLACK_2,
    WHITE_2,
    X,
    Y,
    OMEGA_RUBY,
    ALPHA_SAPPHIRE,
    SUN,
    MOON,
    ULTRA_SUN,
    ULTRA_MOON,
    LETS_GO_PIKACHU,
    LETS_GO_EEVEE,
    SWORD,
    SHIELD,
    THE_ISLE_OF_ARMOR,
    THE_CROWN_TUNDRA,
    BRILLIANT_DIAMOND,
    SHINING_PEARL,
    LEGENDS_ARCEUS,
    SCARLET,
    VIOLET;

    override fun toString(): String {
        return super.toString().replace("_", " ").split(" ")
            .joinToString(" ") { it.lowercase().capitalize(Locale.ROOT) }
    }
}