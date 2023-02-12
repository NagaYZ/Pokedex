package fr.uge.pokedex

import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4
import fr.uge.pokedex.data.Generation
import fr.uge.pokedex.data.PokemonRepository
import fr.uge.pokedex.data.Type

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*

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
            assertFalse(pokemon.description.isBlank())
        }
    }

    @Test
    fun testGetAll() {
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        val pokemonRepository = PokemonRepository(appContext, maxGeneration = Generation.GENERATION_I)

        val pokemon = pokemonRepository.getAll()
        assertFalse(pokemon.isEmpty())
        assertEquals(Generation.GENERATION_I.maxId, pokemon.size)
    }
}