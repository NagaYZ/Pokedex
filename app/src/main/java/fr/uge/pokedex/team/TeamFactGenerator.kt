package fr.uge.pokedex.team

import fr.uge.pokedex.data.*
import fr.uge.pokedex.util.BaseStatHelper
import fr.uge.pokedex.util.BaseStatHelper.Companion.getStandardDeviation
import fr.uge.pokedex.util.BaseStatHelper.Companion.getTotal

class TeamFactGenerator {
    fun getTeamFacts(team: List<Pokemon>) : List<TeamFact> {
        val facts = ArrayList<TeamFact>()

        if(team.size != 6) {
            facts.add(TeamFact.EMPTY_SLOTS)
        }
        facts.addAll(getBaseStatsTeamFacts(team))
        facts.addAll(getTypeTeamFacts(team))

        return facts
    }

    private fun getBaseStatsTeamFacts(members: List<Pokemon>): Collection<TeamFact> {
        val facts = ArrayList<TeamFact>()
        val baseStatsAverage = BaseStatHelper.getAverageBaseStats(members.map { it.baseStats })
        val total = baseStatsAverage.getTotal()
        val standardDeviation = baseStatsAverage.getStandardDeviation()

        if(total <= 350) {
            facts.add(TeamFact.LOW_BASE_STATS)
        }

        if(total >= 530) {
            facts.add(TeamFact.HIGH_BASE_STATS)
        }

        if(standardDeviation >= 20) {
            facts.add(TeamFact.IMBALANCED_BASE_STATS)
        }

        if(standardDeviation <= 10) {
            facts.add(TeamFact.BALANCED_BASE_STATS)
        }

        return facts
    }

    private fun getTypeTeamFacts(members: List<Pokemon>): Collection<TeamFact> {
        val facts = ArrayList<TeamFact>()

        // Weakness of 3 or more members
        val teamWeaknesses = members.map { it.getWeaknesses() }
            .flatten().groupingBy { it }.eachCount().filter { it.value >= 3 }

        // Same but for resistance
        val teamResistance = members.map { it.getResistances() }
            .flatten().groupingBy { it }.eachCount().filter { it.value >= 3 }

        if(teamWeaknesses.isNotEmpty()) {
            facts.add(TeamFact.WEAKNESS_TO_TYPES.setMessage(teamWeaknesses.keys.toString()))
        } else {
            facts.add(TeamFact.GOOD_TYPE_COVERAGE)
        }

        if(teamResistance.isNotEmpty()) {
            facts.add(TeamFact.RESISTANCE_TO_TYPES.setMessage(teamResistance.keys.toString()))
        }

        return facts
    }
}