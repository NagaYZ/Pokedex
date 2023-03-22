package fr.uge.pokedex

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import fr.uge.pokedex.data.pokedex.move.MoveRepository
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MoveRepositoryTest {
    @Test
    fun testGetMove() {
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        val moveRepository = MoveRepository(appContext)

        println(moveRepository.get(1L))
    }
}