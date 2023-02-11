package fr.uge.pokedex.data

import android.content.Context

data class Pokemon(
    val id: Long,
    val name: String,
    var type: Pair<Type, Type>,
    val height: Int,
    val weight: Int,
    var description: String
) {
    fun getSprite(context: Context): Int {
        // Temporary until a better alternative is found.
        return context.resources.getIdentifier("$id", "drawable", context.packageName)
    }
}
