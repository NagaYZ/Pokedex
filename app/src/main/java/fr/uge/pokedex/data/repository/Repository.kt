package fr.uge.pokedex.data.repository

import android.content.Context
import fr.uge.pokedex.data.parser.CsvParser

interface Repository<T> {
    val parser: CsvParser<T>
    val context : Context
    var data : Map<Long, T>

    fun get(id: Long) : T? {
        return data[id]
    }

    fun getAll() : Collection<T> {
        return data.values
    }

    fun find(filter: (T) -> Boolean) : T? {
        return getAll().find(filter)
    }

    fun findAll(filter: (T) -> Boolean) : Collection<T> {
        return getAll().filter(filter)
    }
}