package fr.uge.pokedex.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import fr.uge.pokedex.data.Pokemon
import fr.uge.pokedex.data.PokemonRepository


@Composable
fun SearchBar( pokemons: List<Pokemon>) : List<Pokemon>{
    var search by remember {
        mutableStateOf("")
    }
    var result_List by remember {
        mutableStateOf(mutableListOf<Pokemon>())
    }
    Row(
        modifier = Modifier.fillMaxWidth(),
    ) {
        TextField(
            modifier = Modifier.weight(0.5f),
            value = search,
            onValueChange = { search = it },
            placeholder = { Text("Recherche par nom") }
        )
        result_List = pokemons.filter{it.name.contains(search)}.toMutableList()

        IconButton(onClick = {}) {
            Icon(Icons.Default.Search, contentDescription = "Recherche")
        }
    }
    return result_List
}


@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    Column() {
        var res = SearchBar(PokemonRepository(LocalContext.current).getAll().toList())

        DisplayPokedex(context = LocalContext.current, pokemons = res)
    }




}