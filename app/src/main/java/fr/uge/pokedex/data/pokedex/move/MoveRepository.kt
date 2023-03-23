package fr.uge.pokedex.data.pokedex.move

import android.content.Context
import fr.uge.pokedex.data.pokedex.Repository

class MoveRepository(
    context: Context
) : Repository<Move> {
    override lateinit var data: Map<Long, Move>
    private val parser = MoveParser(context)

    init {
        data = parser.loadData()
    }
}

