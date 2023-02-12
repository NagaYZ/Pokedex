package fr.uge.pokedex.data

// Languages used in the csv data
enum class Language {
    JAPANESE,
    ROMAJI,
    KOREAN,
    CHINESE,
    FRENCH,
    GERMAN,
    SPANISH,
    ITALIAN,
    ENGLISH,
    CZECH;

    fun getLanguage(id: Int): Language {
        return Language.values()[id - 1]
    }
}