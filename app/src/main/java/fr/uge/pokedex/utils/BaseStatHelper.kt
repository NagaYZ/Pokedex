package fr.uge.pokedex.utils

import fr.uge.pokedex.data.pokedex.BaseStats
import fr.uge.pokedex.data.pokedex.Stat
import kotlin.math.pow
import kotlin.math.sqrt

class BaseStatHelper {
    companion object {
        private fun calculateStandardDeviation(values: List<Int>): Double {
            var standardDeviation = 0.0

            val mean = values.average()
            for (value in values) {
                standardDeviation += (value - mean).pow(2.0)
            }

            return sqrt(standardDeviation / values.size)
        }

        private fun BaseStats.getAsList(): List<Int> {
            return listOf(
                hp,
                attack,
                defense,
                specialAttack,
                specialDefense,
                speed
            )
        }

        fun BaseStats.getTotal(): Int {
            return getAsList().sum()
        }

        fun BaseStats.getAverage(): Double {
            return getAsList().average()
        }

        fun BaseStats.getStandardDeviation(): Double {
            return calculateStandardDeviation(getAsList())
        }

        fun getAverageBaseStats(memberBaseStats: List<BaseStats>): BaseStats {
            val baseStatsMap = HashMap<Stat, MutableList<Float>>()
            for(stat in Stat.values()) {
                baseStatsMap[stat] = ArrayList()
            }

            for(baseStats in memberBaseStats) {
                baseStatsMap[Stat.HP]?.add(baseStats.hp.toFloat())
                baseStatsMap[Stat.ATTACK]?.add(baseStats.attack.toFloat())
                baseStatsMap[Stat.DEFENSE]?.add(baseStats.defense.toFloat())
                baseStatsMap[Stat.SPECIAL_ATTACK]?.add(baseStats.specialAttack.toFloat())
                baseStatsMap[Stat.SPECIAL_DEFENSE]?.add(baseStats.specialDefense.toFloat())
                baseStatsMap[Stat.SPEED]?.add(baseStats.speed.toFloat())
            }

            return BaseStats(
                hp = (baseStatsMap[Stat.HP]?.average() ?: 0).toInt(),
                attack = (baseStatsMap[Stat.ATTACK]?.average() ?: 0).toInt(),
                defense = (baseStatsMap[Stat.DEFENSE]?.average() ?: 0).toInt(),
                specialAttack = (baseStatsMap[Stat.SPECIAL_ATTACK]?.average() ?: 0).toInt(),
                specialDefense = (baseStatsMap[Stat.SPECIAL_DEFENSE]?.average() ?: 0).toInt(),
                speed = (baseStatsMap[Stat.SPEED]?.average() ?: 0).toInt(),
            )
        }
    }
}