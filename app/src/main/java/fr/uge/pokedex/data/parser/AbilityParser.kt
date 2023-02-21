package fr.uge.pokedex.data.parser

import android.content.Context
import fr.uge.pokedex.data.Ability

class AbilityParser(override var context: Context) : CsvParser<Ability> {
    override fun loadData(): Map<Long, Ability> {
        TODO("Not yet implemented")
    }
}