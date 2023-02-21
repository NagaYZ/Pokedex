package fr.uge.pokedex.data.repository

import android.content.Context
import fr.uge.pokedex.data.Generation
import fr.uge.pokedex.data.Language
import fr.uge.pokedex.data.Pokemon
import fr.uge.pokedex.data.parser.CsvParser
import fr.uge.pokedex.data.parser.PokemonParser

class PokemonRepository(
    override var context: Context,
    languageData: Language = Language.ENGLISH,
    maxGeneration: Generation = Generation.GENERATION_VII
) : Repository<Pokemon> {
    override lateinit var data: Map<Long, Pokemon>
    override val parser: CsvParser<Pokemon> = PokemonParser(context, languageData, maxGeneration)

    init {
        data = parser.loadData()
    }
}

