package fr.uge.pokedex

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import fr.uge.pokedex.data.pokedex.location.LocationRepository
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class LocationRepositoryTest {
    @Test
    fun testGetLocation() {
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        val locationRepository = LocationRepository(appContext)

        println(locationRepository.get(3L))
    }
}