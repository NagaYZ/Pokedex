package fr.uge.pokedex.data.parser

import android.content.Context
import fr.uge.pokedex.data.Move

class MoveParser(override var context: Context) : CsvParser<Move> {
    override fun loadData(): Map<Long, Move> {
        TODO("Not yet implemented")
    }
}