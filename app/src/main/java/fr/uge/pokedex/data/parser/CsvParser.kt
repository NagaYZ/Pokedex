package fr.uge.pokedex.data.parser

import android.content.Context

interface CsvParser<T> {
    var context: Context

    fun loadData() : Map<Long, T>

    fun parseLines(filename: String, action: (String) -> Unit) {
        val reader = context.assets.open(filename).bufferedReader()
        val header = reader.readLine()

        reader.lineSequence().toList()
            .filter { it.isNotBlank() }
            .forEach(action)
    }
}