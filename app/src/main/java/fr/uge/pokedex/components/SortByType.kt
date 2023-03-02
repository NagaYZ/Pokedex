package fr.uge.pokedex.components

import androidx.compose.foundation.layout.Row

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import fr.uge.pokedex.data.Pokemon
import fr.uge.pokedex.data.Type
import kotlinx.coroutines.flow.merge

@Composable
fun SortByType(pokemons: List<Pokemon>, name: String, typePos: String) : List<Pokemon> {

    var types by remember {
        mutableStateOf(Type.values().toMutableList())
    }

    var resultList by remember {
        mutableStateOf(pokemons.toMutableList())
    }
    var state by remember {
        mutableStateOf(false)
    }
    var currentType by remember {
        mutableStateOf("")
    }
    Row(

    ) {
        IconButton(onClick = {
            state = true
        }){
            Row() {

                Text(text = if (currentType != "") currentType else name)


                Icon(
                    imageVector = Icons.Default.ArrowDropDown,
                    contentDescription = "Open Filter"
                )
            }
            DropdownMenu(
                expanded = state,
                onDismissRequest = {
                    state = false
                }
            ) {
                types.forEach { nameType ->
                    DropdownMenuItem(
                        onClick = {
                            resultList.clear()
                            currentType = nameType.toString()
                            state = false
                        },
                    ) {
                        Text(text = nameType.toString())
                    }
                }
            }
        }
    }
    if(currentType != ""){
        if(typePos == "first") {
            pokemons.forEach { pokemon ->
                if (pokemon.type.first.toString().equals(currentType)) {
                    resultList.add(pokemon)
                }
            }
        }
        if(typePos == "second"){
            pokemons.forEach { pokemon ->
                if (pokemon.type.second.toString().equals(currentType)) {
                    resultList.add(pokemon)
                }
            }
        }
    }
    return resultList
}

@Composable
fun TwoFilters(pokemons: List<Pokemon>) : List<Pokemon>{
    var resultList1 by remember {
        mutableStateOf(pokemons.toList())
    }
    var resultList2 by remember {
        mutableStateOf(pokemons.toList())
    }
    Row(Modifier.fillMaxWidth()) {
        resultList1 = SortByType(pokemons = pokemons, name = "Type 1", "first")
        resultList2 = SortByType(pokemons = pokemons, name = "Type 2", "second")
    }
    return resultList1.intersect(resultList2).toList()
}