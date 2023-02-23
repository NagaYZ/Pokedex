package fr.uge.pokedex.data

import android.annotation.SuppressLint
import android.content.Context
import java.util.*

data class Pokemon(
    val id: Long,
    val identifier: String,
    val height: Int, // unit is 1/10th of a meter, ex: 7 = 0.7m
    val weight: Int, // unit is 1/10th of a kilogram, ex: 69 = 6.9kg
    var type: Pair<Type, Type> = Pair(Type.NONE, Type.NONE),
    var name: String = "",
    val descriptions: MutableMap<Version, String> = EnumMap(Version::class.java),
    var genus: String = "",
    val locations: MutableMap<Version, MutableSet<Location>> = EnumMap(Version::class.java),
    var evolvesFrom: Evolution? = null,
    var evolvesInto: MutableSet<Evolution> = HashSet(),
    var eggGroups: MutableSet<EggGroup> = HashSet(),
    var baseExperience: Int = 0,
    var captureRate: Int = 0, // 0 to 255
    var baseHappiness: Int = 50, // 0 to 255
    var hatchCounter: Int = 20, // number of cycles (steps) for an egg to hatch
    var growRate: GrowRate = GrowRate.MEDIUM,
    val baseStats: Stats = Stats(),
    val abilities: Abilities = Abilities()
) {

    @SuppressLint("DiscouragedApi")
    @Suppress("unused")
    fun getSprite(context: Context): Int {
        return context.resources.getIdentifier("pokemon_$id", "drawable", context.packageName)
    }

    @SuppressLint("DiscouragedApi")
    @Suppress("unused")
    fun getIcon(context: Context): Int {
        return context.resources.getIdentifier("icon_pkm_$id", "drawable", context.packageName)
    }
}
