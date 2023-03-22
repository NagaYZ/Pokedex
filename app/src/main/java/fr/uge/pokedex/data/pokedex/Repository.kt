package fr.uge.pokedex.data.pokedex

interface Repository <E> {
    var data: Map<Long, E>

    fun get(id: Long): E? {
        return data[id]
    }

    fun getAll(): Collection<E> {
        return data.values
    }
}