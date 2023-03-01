package fr.uge.pokedex.team

import fr.uge.pokedex.data.BaseStats
import fr.uge.pokedex.data.Pokemon
import fr.uge.pokedex.data.PokemonRepository
import fr.uge.pokedex.data.Stat
import fr.uge.pokedex.database.TeamWithMembers
import fr.uge.pokedex.util.BaseStatHelper.Companion.getStandardDeviation
import fr.uge.pokedex.util.BaseStatHelper.Companion.getTotal

class TeamFactGenerator(private val pokemonRepository: PokemonRepository) {
    fun getTeamFact(teams: TeamWithMembers) : List<TeamFact> {
        val facts = ArrayList<TeamFact>()
        val members = teams.teamMembers.map { pokemonRepository.get(it.getPokemonId())!! }

        if(teams.teamMembers.size != 6) {
            facts.add(TeamFact("Your team has empty slot", FactCategory.NEITHER))
        }
        facts.addAll(getBaseStatsTeamFact(members))
        facts.addAll(getTypeTeamFact(members))

        return facts
    }

    private fun getBaseStatsTeamFact(members: List<Pokemon>): Collection<TeamFact> {
        val facts = ArrayList<TeamFact>()
        val baseStatsAverage = getTeamAverageBaseStats(members)
        val total = baseStatsAverage.getTotal()
        val standardDeviation = baseStatsAverage.getStandardDeviation()

        if(total <= 300) {
            facts.add(TeamFact("Low base stats", FactCategory.WEAKNESS))
        }

        if(total >= 530) {
            facts.add(TeamFact("High base stats", FactCategory.STRENGTH))
        }

        if(standardDeviation >= 25) {
            facts.add(TeamFact("Imbalanced base stats", FactCategory.WEAKNESS))
        }

        if(standardDeviation <= 10) {
            facts.add(TeamFact("Balanced base stats", FactCategory.STRENGTH))
        }

        return emptyList()
    }

    private fun getTypeTeamFact(members: List<Pokemon>): Collection<TeamFact> {


        return emptyList()
    }

    private fun getTeamAverageBaseStats(members: List<Pokemon>): BaseStats {
        val baseStats = HashMap<Stat, MutableList<Float>>()
        for(stat in Stat.values()) {
            baseStats[stat] = ArrayList()
        }

        for(pokemon in members) {
            baseStats[Stat.HP]?.add(pokemon.baseStats.hp.toFloat())
            baseStats[Stat.ATTACK]?.add(pokemon.baseStats.attack.toFloat())
            baseStats[Stat.DEFENSE]?.add(pokemon.baseStats.defense.toFloat())
            baseStats[Stat.SPECIAL_ATTACK]?.add(pokemon.baseStats.specialAttack.toFloat())
            baseStats[Stat.SPECIAL_DEFENSE]?.add(pokemon.baseStats.specialDefense.toFloat())
            baseStats[Stat.SPEED]?.add(pokemon.baseStats.speed.toFloat())
        }

        return BaseStats(
            hp = (baseStats[Stat.HP]?.average() ?: 0) as Int,
            attack = (baseStats[Stat.ATTACK]?.average() ?: 0) as Int,
            defense = (baseStats[Stat.DEFENSE]?.average() ?: 0) as Int,
            specialAttack = (baseStats[Stat.SPECIAL_ATTACK]?.average() ?: 0) as Int,
            specialDefense = (baseStats[Stat.SPECIAL_DEFENSE]?.average() ?: 0) as Int,
            speed = (baseStats[Stat.SPEED]?.average() ?: 0) as Int,
        )
    }
}