package fr.uge.pokedex.data.repository

import android.content.Context
import fr.uge.pokedex.data.Move
import fr.uge.pokedex.data.parser.CsvParser
import fr.uge.pokedex.data.parser.MoveParser

class MoveRepository(override var context: Context) : Repository<Move> {
    override lateinit var data: Map<Long, Move>
    override val parser : CsvParser<Move> = MoveParser(context)

    init {
        data = parser.loadData()
    }
}