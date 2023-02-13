package fr.uge.pokedex.data

import android.content.Context
import androidx.compose.material.Text
import java.io.File

data class Pokemon(
    val id: Long,
    val identifier: String,
    val type: Pair<Type, Type>,
    val height: Int,
    val weight: Int,
    var name: String,
    val description: String,
    val genus: String,
    val hasIcon: Boolean
) {
    fun getSprite(context: Context): Int {
        return context.resources.getIdentifier("pokemon_$id", "drawable", context.packageName)
    }

    fun getIcon(context: Context): Int {

        val iconExist = context.resources.getIdentifier("icon_pkm_$id", "drawable", context.packageName)

        if(iconExist == 0){
            return context.resources.getIdentifier("pokemon_0", "drawable", context.packageName)
        }
        return iconExist
    }

        class Builder {
            var id: Long? = null
            var identifier: String? = null
            var type: Pair<Type, Type> = Pair(Type.NONE, Type.NONE)
            var height: Int? = null
            var weight: Int? = null
            var name: String? = null
            var description: String? = null
            var genus: String? = null
            var hasIcon: Boolean? = true

            fun id(id: Long) = apply { this.id = id }
            fun identifier(identifier: String) = apply { this.identifier = identifier }
            fun firstType(type: Type) = apply { this.type = this.type.copy(first = type) }
            fun secondType(type: Type) = apply { this.type = this.type.copy(second = type) }
            fun height(height: Int) = apply { this.height = height }
            fun weight(weight: Int) = apply { this.weight = weight }
            fun name(name: String) = apply { this.name = name }
            fun description(description: String) = apply { this.description = description }
            fun genus(genus: String) = apply { this.genus = genus }
            fun hasIcon(hasIcon: Boolean) = apply { this.hasIcon = hasIcon }

            fun build() = Pokemon(
                id!!, identifier!!, type, height!!, weight!!, name!!,
                description!!, genus!!, hasIcon!!
            )
        }
    }


