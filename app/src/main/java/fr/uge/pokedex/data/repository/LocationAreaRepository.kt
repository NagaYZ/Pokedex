package fr.uge.pokedex.data.repository

import android.content.Context
import fr.uge.pokedex.data.LocationArea
import fr.uge.pokedex.data.parser.CsvParser
import fr.uge.pokedex.data.parser.LocationAreaParser

class LocationAreaRepository(override var context: Context) : Repository<LocationArea> {
    override lateinit var data: Map<Long, LocationArea>
    override val parser : CsvParser<LocationArea> = LocationAreaParser(context)

    init {
        data = parser.loadData()
    }
}