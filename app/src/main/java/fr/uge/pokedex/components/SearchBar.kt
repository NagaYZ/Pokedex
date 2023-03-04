package fr.uge.pokedex.components

import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp


@Composable
fun SearchBar(pokemonSearch: (String) -> Unit) {
    var search by remember {
        mutableStateOf("")
    }

    Icon(Icons.Default.Search, contentDescription = "Recherche", modifier = Modifier.size(30.dp, 60.dp) )

    TextField(
        modifier = Modifier.size(200.dp, 60.dp),
        value = search,
        onValueChange = { search = it },
        placeholder = { Text("Recherche par nom") }
    )
    pokemonSearch(search)

}
