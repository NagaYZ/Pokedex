package fr.uge.pokedex.data.repository

import android.content.Context
import fr.uge.pokedex.data.Ability
import fr.uge.pokedex.data.parser.CsvParser
import fr.uge.pokedex.data.parser.AbilityParser

class AbilityRepository(override var context: Context) : Repository<Ability> {
    override lateinit var data: Map<Long, Ability>
    override val parser : CsvParser<Ability> = AbilityParser(context)

    init {
        data = parser.loadData()
    }
}