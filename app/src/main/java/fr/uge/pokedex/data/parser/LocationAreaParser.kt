package fr.uge.pokedex.data.parser

import android.content.Context
import fr.uge.pokedex.data.LocationArea

class LocationAreaParser(
    override var context: Context
) : CsvParser<LocationArea> {

    override fun loadData(): Map<Long, LocationArea> {
        TODO("Not yet implemented")
    }
}