package fr.uge.pokedex

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import fr.uge.pokedex.data.*
import fr.uge.pokedex.data.pokedex.*
import fr.uge.pokedex.data.pokedex.pokemon.*
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
        assertEquals(pokemon?.icon, R.drawable.icon_pkm_1)
        assertEquals(pokemon?.sprite, R.drawable.pokemon_1)
        assertEquals(Pair(Type.GRASS, Type.POISON), pokemon?.type)
        if (pokemon != null) {
            assertFalse(pokemon.genus.isBlank())
            assertTrue(pokemon.baseHappiness == 50)
            assertTrue(pokemon.baseExperience == 64)
            assertTrue(pokemon.eggGroups.contains(EggGroup.MONSTER))
            assertTrue(pokemon.eggGroups.contains(EggGroup.PLANT))
            assertTrue(pokemon.captureRate == 45)
            assertTrue(pokemon.growRate == GrowRate.MEDIUM_SLOW)
            assertTrue(pokemon.hatchCounter == 20)

            val baseStats = BaseStats(
                hp = 45,
                attack = 49,
                defense = 49,
                specialAttack = 65,
                specialDefense = 65,
                speed = 45
            )
            assertTrue(pokemon.baseStats == baseStats)
            assertFalse(pokemon.pokedexEntries.isEmpty())
        }
    }

    @Test
    fun testGetPokemon2() {
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        val pokemonRepository =
            PokemonRepository(appContext)

        val pokemon = pokemonRepository.get(359)
        println(pokemon?.evolutionChainId ?: "well")
    }

    @Test
    fun testGetAll() {
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        val pokemonRepository =
            PokemonRepository(appContext)

        val pokemon = pokemonRepository.getAll()
        println(pokemon.size)
        assertFalse(pokemon.isEmpty())
    }
}