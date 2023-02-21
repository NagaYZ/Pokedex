package fr.uge.pokedex.data

data class LocationArea(
    val id: Long,
    val region: Region,
    var areaName: String = "",
    var locationName: String = ""
)
