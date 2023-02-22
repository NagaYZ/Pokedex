package fr.uge.pokedex.data

import android.annotation.SuppressLint
import android.content.Context
import java.util.*

data class Pokemon(
    val id: Long,
    val identifier: String,
    val height: Int, // unit is 1/10th of a m, ex: 7 = 0.7m
    val weight: Int, // unit is 1/10th of a kg, ex: 69 = 6.9kg
    var type: Pair<Type, Type> = Pair(Type.NONE, Type.NONE),
    var name: String = "",
    val descriptions: MutableMap<Version, String> = EnumMap(Version::class.java),
    var genus: String = "",
    val locations: MutableSet<Location> = HashSet(),
    var evolvesFrom: Evolution? = null,
    var evolvesInto: Evolution? = null
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
