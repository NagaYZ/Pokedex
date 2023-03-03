package fr.uge.pokedex.data

import android.annotation.SuppressLint
import android.content.Context

import java.util.*
import kotlin.collections.HashSet


data class Pokemon(
    val id: Long,
    val identifier: String,
    val height: Int, // unit is 1/10th of a meter, ex: 7 = 0.7m
    val weight: Int, // unit is 1/10th of a kilogram, ex: 69 = 6.9kg
    var type: Pair<Type, Type> = Pair(Type.NONE, Type.NONE),
    var name: String = "",
    val pokedexEntries: MutableSet<FlavorText> = HashSet(),
    var genus: String = "",
    val encounters: MutableSet<Encounter> = HashSet(),
    var evolvesFrom: Evolution? = null,
    var evolvesInto: MutableSet<Evolution> = HashSet(),
    var eggGroups: MutableSet<EggGroup> = HashSet(),
    var baseExperience: Int = 0,
    var captureRate: Int = 0, // 0 to 255
    var baseHappiness: Int = 50, // 0 to 255
    var hatchCounter: Int = 20, // number of cycles (steps) for an egg to hatch
    var growRate: GrowRate = GrowRate.MEDIUM,
    val baseStats: BaseStats = BaseStats(),
    val abilities: Abilities = Abilities(),
    val learnSet: MutableSet<LearnableMove> = HashSet()
) {

    @SuppressLint("DiscouragedApi")
    @Suppress("unused")
    fun getSprite(context: Context): Int {
        return context.resources.getIdentifier("pokemon_$id", "drawable", context.packageName)
    }

    @SuppressLint("DiscouragedApi")
    @Suppress("unused")
    fun getIcon(context: Context): Int {

        val iconExist =
            context.resources.getIdentifier("icon_pkm_$id", "drawable", context.packageName)

        if (iconExist == 0) {
            return context.resources.getIdentifier("pokemon_0", "drawable", context.packageName)
        }
        return iconExist
    }

    fun getWeaknesses(): Set<Type> {
        val weaknessFirst = type.first.isVulnerableTo.subtract(type.second.isResistantTo)
        val weaknessSecond = type.second.isVulnerableTo.subtract(type.first.isResistantTo)
        return weaknessFirst.union(weaknessSecond)
    }

    fun getResistances(): Set<Type> {
        val resistanceFirst = type.first.isResistantTo.subtract(type.second.isVulnerableTo)
        val resistanceSecond = type.second.isResistantTo.subtract(type.first.isVulnerableTo)
        return resistanceFirst.union(resistanceSecond)

    }
}

