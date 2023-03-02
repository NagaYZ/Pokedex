package fr.uge.pokedex.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import fr.uge.pokedex.data.Pokemon
import fr.uge.pokedex.data.PokemonRepository
import fr.uge.pokedex.data.Type

@Composable
fun SortByType(pokemons: List<Pokemon>) : List<Pokemon> {
    val types = Type.values()
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
        modifier = Modifier.fillMaxWidth(),

    ) {
        IconButton(onClick = {
            state = true
        },
            modifier = Modifier.weight(1/3f)) {
            Row() {
                Text(text = "Type")
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
        pokemons.forEach { pokemon ->
            if (pokemon.type.first.toString().equals(currentType) || pokemon.type.second.toString().equals(currentType)) {
                resultList.add(pokemon)
            }
        }
    }
    for(i in resultList){
        println(i.toString())
    }
    return resultList
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    var currentPokemon by remember {
        mutableStateOf(Pokemon(-1L, "", Pair(Type.NONE, Type.NONE), 0,0,"","", "", true))
    }
    val navController: NavHostController = rememberNavController()

    DisplayPokedex(context = LocalContext.current, pokemons = SortByType(

        pokemons = PokemonRepository(LocalContext.current).getAll().toList())
        , navController) {
        currentPokemon = it
    }
}