package fr.uge.pokedex.utils.parser

import android.content.Context
import com.github.doyaaaaaken.kotlincsv.dsl.csvReader

interface Parser <E> {
    val context: Context

    fun loadData(): Map<Long, E>;

    fun parseLines(filename: String, action: (Map<String, String>) -> Unit) {
        csvReader().open(context.assets.open(filename)) {
            readAllWithHeaderAsSequence().forEach(action)
        }
    }
}