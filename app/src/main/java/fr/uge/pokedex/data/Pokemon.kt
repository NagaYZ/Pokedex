package fr.uge.pokedex.data

data class Pokemon(val id : Long, val name : String, val type : Pair<Type, Type>)
