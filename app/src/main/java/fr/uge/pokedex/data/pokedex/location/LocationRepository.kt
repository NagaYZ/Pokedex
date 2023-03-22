package fr.uge.pokedex.data.pokedex.location

import android.content.Context
import fr.uge.pokedex.data.pokedex.Repository
import fr.uge.pokedex.utils.parser.LocationParser

class LocationRepository(
    context: Context
) : Repository<Location> {
    override lateinit var data: Map<Long, Location>
    private val parser = LocationParser(context)

    init {
        data = parser.loadData()
    }
}

