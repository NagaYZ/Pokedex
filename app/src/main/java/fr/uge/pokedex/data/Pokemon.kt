package fr.uge.pokedex.data

import android.content.Context

data class Pokemon(
    val id: Long,
    val identifier: String,
    val height: Int,
    val weight: Int,
    var type: Pair<Type, Type> = Pair(Type.NONE, Type.NONE),
    var name: String = "",
    var description: String = "",
    var genus: String = ""
) {
    fun getSprite(context: Context): Int {
        return context.resources.getIdentifier("pokemon_$id", "drawable", context.packageName)
    }

    fun getIcon(context: Context): Int {
        return context.resources.getIdentifier("icon_pkm_$id", "drawable", context.packageName)
    }
}
