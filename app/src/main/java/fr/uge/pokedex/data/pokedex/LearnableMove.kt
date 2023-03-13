package fr.uge.pokedex.data.pokedex

data class LearnableMove(
    val move: Move,
    val method: MoveLearnMethod,
    val level: Int
)
