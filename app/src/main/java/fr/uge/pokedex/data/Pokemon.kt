package fr.uge.pokedex.data

data class Pokemon(val id : Long, val name : String, var type : Pair<Type, Type>, val height : Int, val weight : Int)
