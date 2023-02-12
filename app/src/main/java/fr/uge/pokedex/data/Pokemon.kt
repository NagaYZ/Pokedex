package fr.uge.pokedex.data

import android.content.Context

data class Pokemon(
    val id: Long,
    val identifier: String,
    var type: Pair<Type, Type>,
    val height: Int,
    val weight: Int,
    var description: String
) {
    fun getSprite(context: Context): Int {
        return context.resources.getIdentifier("pokemon_$id", "drawable", context.packageName)
    }

    fun getIcon(context: Context): Int {
        return context.resources.getIdentifier("icon_pkm_$id", "drawable", context.packageName)
    }
}
