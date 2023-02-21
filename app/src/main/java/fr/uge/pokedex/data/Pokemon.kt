package fr.uge.pokedex.data

import android.content.Context

data class Pokemon(
    val id: Long,
    val identifier: String,
    val height: Int, // unit is 1/10th of a m, ex: 7 = 0.7m
    val weight: Int, // unit is 1/10th of a kg, ex: 69 = 6.9kg
    var type: Pair<Type, Type> = Pair(Type.NONE, Type.NONE),
    var name: String = "",
    var description: String = "",
    var genus: String = "",
    var locations: MutableSet<LocationArea> = HashSet(),
    var evolvesFrom: Evolution? = null,
    var evolvesInto: Evolution? = null
) {
    fun getSprite(context: Context): Int {
        return context.resources.getIdentifier("pokemon_$id", "drawable", context.packageName)
    }

    fun getIcon(context: Context): Int {
        return context.resources.getIdentifier("icon_pkm_$id", "drawable", context.packageName)
    }
}
