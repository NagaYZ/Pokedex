package fr.uge.pokedex.data.pokedex.ability

import android.content.Context
import fr.uge.pokedex.data.pokedex.Repository
import fr.uge.pokedex.utils.parser.AbilityParser

class AbilityRepository(
    context: Context
) : Repository<Ability> {
    override lateinit var data: Map<Long, Ability>
    private val parser = AbilityParser(context)

    init {
        data = parser.loadData()
    }
}

