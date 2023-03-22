package fr.uge.pokedex

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import fr.uge.pokedex.data.pokedex.evolution.EvolutionChainRepository
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class EvolutionChainRepositoryTest {
    @Test
    fun testGetEvolutionChain() {
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        val evolutionChainRepository = EvolutionChainRepository(appContext)

        println(evolutionChainRepository.get(2L))
    }
}