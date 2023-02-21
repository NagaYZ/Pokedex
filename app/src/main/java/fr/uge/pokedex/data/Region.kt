package fr.uge.pokedex.data

enum class Region {
    KANTO, JOHTO, HOENN, SINNOH, UNOVA, KALOS, ALOLA, GALAR, HISUI, PALDEA;

    companion object {
        fun getRegion(id: Int): Region {
            return Region.values()[id - 1]
        }
    }
}