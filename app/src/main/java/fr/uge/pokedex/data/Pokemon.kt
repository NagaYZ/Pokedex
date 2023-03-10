package fr.uge.pokedex.data

import kotlin.collections.HashSet


data class Pokemon(
    val id: Long,
    val identifier: String,
    val icon: Int,
    val sprite: Int,
    val height: Int = 0, // unit is 1/10th of a meter, ex: 7 = 0.7m
    val weight: Int = 0, // unit is 1/10th of a kilogram, ex: 69 = 6.9kg
    var type: Pair<Type, Type> = Pair(Type.NONE, Type.NONE),
    var name: String = "",
    var description: String = "", // Most recent description
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

