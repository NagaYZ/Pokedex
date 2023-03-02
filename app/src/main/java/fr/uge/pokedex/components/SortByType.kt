package fr.uge.pokedex.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import fr.uge.pokedex.data.Pokemon
import fr.uge.pokedex.data.PokemonRepository
import fr.uge.pokedex.data.Type

@Composable
fun SortByType(aligmt: Alignment, pokemonsByType: (Type) -> List<Pokemon>) {
    val types = Type.values()
    
    var state by remember {
        mutableStateOf(false)
    }
    Box(
        modifier = Modifier.fillMaxWidth(),
        contentAlignment = aligmt
    ) {
        IconButton(onClick = {
            state = true
        }) {
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
                            pokemonsByType(nameType)
                            state = false
                        },
                        ) {
                        Text(text = nameType.toString())
                    }
                }
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    var list = PokemonRepository(LocalContext.current).getAll().toList()
    var resultList by remember {
        mutableStateOf(mutableListOf<Pokemon>())
    }
    SortByType(Alignment.TopEnd){
        println("item $it")
        list.forEach {  pokemon -> if(pokemon.type.first.equals(it) || pokemon.type.second.equals(it)) resultList.add(pokemon)}
        for(i in resultList){
            println("poke $i")
        }
        resultList
    }


}
