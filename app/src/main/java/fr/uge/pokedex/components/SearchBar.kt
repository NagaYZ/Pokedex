package fr.uge.pokedex.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import fr.uge.pokedex.data.Pokemon


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

        Icon(Icons.Default.Search, contentDescription = "Recherche", modifier = Modifier.size(30.dp, 60.dp) )

        TextField(
            modifier = Modifier.fillMaxWidth(),
            value = search,
            onValueChange = { search = it },
            placeholder = { Text("Recherche par nom") }
        )

        result_List = pokemons.filter{it.name.contains(if(search.isNotEmpty()) search.replaceFirst(search.first(), search.first().uppercaseChar()) else search)}.toMutableList()

    }
    return result_List
}
