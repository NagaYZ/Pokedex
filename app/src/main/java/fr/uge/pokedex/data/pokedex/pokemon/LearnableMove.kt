package fr.uge.pokedex.data.pokedex.pokemon

data class LearnableMove(
    val moveId: Long,
    val method: MoveLearnMethod,
    val level: Int
)
