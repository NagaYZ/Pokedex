package fr.uge.pokedex.team

// Fact about a team composition. Can be about types, base stats, moves, etc.
data class TeamFact(
    val fact: String,
    val category: FactCategory
)
