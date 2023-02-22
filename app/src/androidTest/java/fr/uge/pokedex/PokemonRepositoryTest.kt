package fr.uge.pokedex

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import fr.uge.pokedex.data.*
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class PokemonRepositoryTest {
    @Test
    fun testGetPokemon() {
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        val pokemonRepository = PokemonRepository(appContext)

        val pokemon = pokemonRepository.get(1)

        assertNotNull(pokemon)
        assertEquals("bulbasaur", pokemon?.identifier)
        assertEquals(Pair(Type.GRASS, Type.POISON), pokemon?.type)
        if (pokemon != null) {
            assertFalse(pokemon.descriptions.values.isEmpty())
            assertFalse(pokemon.genus.isBlank())
            assertTrue(pokemon.evolvesInto.first().evolvedSpeciesId == 2)
            assertNull(pokemon.evolvesFrom)
            assertTrue(pokemon.evolvesInto.first().evolutionTrigger == EvolutionTrigger.LEVEL_UP)
            assertTrue(pokemon.evolvesInto.first().minimumLevel == 16)
            assertTrue(pokemon.baseHappiness == 50)
            assertTrue(pokemon.baseExperience == 64)
            assertTrue(pokemon.eggGroups.contains(EggGroup.MONSTER))
            assertTrue(pokemon.eggGroups.contains(EggGroup.PLANT))
            assertTrue(pokemon.captureRate == 45)
            assertTrue(pokemon.growRate == GrowRate.MEDIUM_SLOW)
            assertTrue(pokemon.hatchCounter == 20)

            val baseStats = Stats(
                hp = 45,
                attack = 49,
                defense = 49,
                specialAttack = 65,
                specialDefense = 65,
                speed = 45
            )
            assertTrue(pokemon.baseStats == baseStats)
        }
    }

    @Test
    fun testGetAll() {
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        val pokemonRepository =
            PokemonRepository(appContext, maxGeneration = Generation.GENERATION_I)

        val pokemon = pokemonRepository.getAll()
        assertFalse(pokemon.isEmpty())
        assertEquals(Generation.GENERATION_I.maxId, pokemon.size)
    }
}