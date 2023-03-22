package fr.uge.pokedex.utils.parser

import android.content.Context
import fr.uge.pokedex.data.pokedex.location.Area
import fr.uge.pokedex.data.pokedex.location.Location
import fr.uge.pokedex.data.pokedex.location.Region

class LocationParser(override val context: Context) : Parser<Location> {
    override fun loadData(): Map<Long, Location> {
        val locations = HashMap<Long, Location>()

        parseLines("csv/core/locations.csv") { row ->
            val id = row["id"]?.toLong()!!
            val region = if (row["region_id"]?.isBlank()!!) Region.UNKNOWN
            else Region.values()[row["region_id"]?.toInt()!! - 1]
            locations[id] = Location(id, region = region)
        }

        parseLines("csv/core/location_names.csv") { row ->
            val locationId = row["location_id"]?.toLong()!!
            val name = row["name"]!!
            locations[locationId]?.name = name
        }

        parseLines("csv/core/location_areas.csv") { row ->
            val id = row["id"]?.toLong()!!
            val locationId = row["location_id"]?.toLong()!!
            locations[locationId]?.area = Area(id)
        }

        val locationsByAreaId = locations.values.associateBy { it.area?.id }

        parseLines("csv/core/location_area_prose.csv") { row ->
            val locationAreaId = row["location_area_id"]?.toLong()!!
            val name = row["name"]!!
            locationsByAreaId[locationAreaId]?.area?.name = name
        }

        return locations
    }
}