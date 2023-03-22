package fr.uge.pokedex.data.pokedex.location

data class Location(
    val id: Long,
    val region: Region,
    var name: String = "",
    var area: Area? = null,
)
