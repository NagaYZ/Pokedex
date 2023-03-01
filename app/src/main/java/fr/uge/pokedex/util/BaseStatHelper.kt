package fr.uge.pokedex.util

import fr.uge.pokedex.data.BaseStats
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
    }
}