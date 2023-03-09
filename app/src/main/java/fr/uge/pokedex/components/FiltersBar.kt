package fr.uge.pokedex.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import fr.uge.pokedex.data.Pokemon


@Composable
fun FiltersBar(pokemonList: List<Pokemon>, filterList: (List<Pokemon>) -> Unit){

    var type1 by remember {
        mutableStateOf("")
    }

    var type2 by remember {
        mutableStateOf("")
    }

    var search by remember {
        mutableStateOf("")
    }

    var resultList by remember {
        mutableStateOf(mutableListOf<Pokemon>())
    }

    Column(Modifier.fillMaxWidth(), verticalArrangement = Arrangement.SpaceBetween, horizontalAlignment = Alignment.CenterHorizontally) {
        SearchBar(pokemonSearch = {
            search = it
        })
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(20.dp)) {
            DropDownType(name = "Type 1", typeNum = {
                type1 = it
            })
            DropDownType(name = "Type 2", typeNum = {
                type2 = it
            })
        }

    }

    if(type1 == "" && type2 == "" && search == ""){
        filterList(pokemonList)
    }

    LaunchedEffect(type1, type2, search){
        resultList.clear()
        for (pokemon in pokemonList){
            if ((pokemon.type.first.toString().contains(type1) || pokemon.type.second.toString().contains(type1))  && (pokemon.type.first.toString().contains(type2) || pokemon.type.second.toString().contains(type2)) ){
                resultList.add(pokemon)
            }
        }

        resultList = resultList.filter {
            it.name.contains(
                if (search.isNotEmpty()) search.replaceFirst(
                    search.first(),
                    search.first().uppercaseChar()
                ) else search
            )
        }.toMutableList()

        filterList(resultList)
    }
}