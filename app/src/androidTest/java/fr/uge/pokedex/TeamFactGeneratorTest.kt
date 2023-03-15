package fr.uge.pokedex

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import fr.uge.pokedex.data.pokedex.PokemonRepository
import fr.uge.pokedex.data.team.TeamFact
import fr.uge.pokedex.data.team.TeamFactGenerator
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class TeamFactGeneratorTest {
    @Test
    fun testGetTeamFacts() {
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        val pokemonRepository = PokemonRepository(appContext)
        val teamFactGenerator = TeamFactGenerator()

        // Team with low base stats and grass type
        val grassTeam = listOf(
            pokemonRepository.get(1)!!, // Bulbasaur
            pokemonRepository.get(152)!!, // Chikorita
            pokemonRepository.get(69)!! // Bellsprout
        )

        var teamFacts = teamFactGenerator.getTeamFacts(grassTeam)
        var expectedTeamFacts = listOf(
            TeamFact.WEAKNESS_TO_TYPES,
            TeamFact.RESISTANCE_TO_TYPES,
            TeamFact.EMPTY_SLOTS,
            TeamFact.LOW_BASE_STATS,
            TeamFact.BALANCED_BASE_STATS
        )
        assertTrue(teamFacts.containsAll(expectedTeamFacts))

        // Extremely imbalanced and high base stats team
        val mewtwoTeam = listOf(
            pokemonRepository.get(150)!!,
            pokemonRepository.get(150)!!,
            pokemonRepository.get(150)!!,
            pokemonRepository.get(150)!!,
            pokemonRepository.get(150)!!,
            pokemonRepository.get(150)!!
        )
        teamFacts = teamFactGenerator.getTeamFacts(mewtwoTeam)
        expectedTeamFacts = listOf(
            TeamFact.HIGH_BASE_STATS,
            TeamFact.IMBALANCED_BASE_STATS,
            TeamFact.WEAKNESS_TO_TYPES,
            TeamFact.RESISTANCE_TO_TYPES
        )
        assertTrue(teamFacts.containsAll(expectedTeamFacts))
    }
}