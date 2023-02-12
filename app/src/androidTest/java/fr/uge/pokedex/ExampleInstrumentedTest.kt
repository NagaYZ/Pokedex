package fr.uge.pokedex

import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4
import fr.uge.pokedex.data.PokemonRepository
import fr.uge.pokedex.data.Type

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {
    @Test
    fun useAppContext() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext


        assertEquals("fr.uge.pokedex", appContext.packageName)
    }

    @Test
    fun testGetPokemon() {
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        val pokemonRepository = PokemonRepository(appContext)

        val pokemon = pokemonRepository.get(1)
        assertNotNull(pokemon)
        assertEquals("bulbasaur", pokemon?.identifier)
        assertEquals(Pair(Type.GRASS, Type.POISON), pokemon?.type)
    }

    @Test
    fun testGetAll() {
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        val pokemonRepository = PokemonRepository(appContext)

        val pokemon = pokemonRepository.getAll()
        assertNotEquals(0, pokemon.size)
    }
}