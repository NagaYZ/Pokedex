package fr.uge.pokedex.data

data class LearnableMove(
    val move: Move,
    val method: MoveLearnMethod,
    val level: Int
)
