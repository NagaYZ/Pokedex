package fr.uge.pokedex.components

import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp


@Preview
@Composable
fun SearchBar(pokemonSearch: (String) -> Unit = {}) {
    var search by remember {
        mutableStateOf("")
    }

    Icon(
        Icons.Default.Search,
        contentDescription = "Search",
        modifier = Modifier.size(30.dp, 30.dp),
        tint = Color.LightGray
    )
    TextField(
        modifier = Modifier.size(200.dp, 30.dp),
        value = search,
        onValueChange = { search = it },
        placeholder = {

            Text("Search by name")
        }
    )

    pokemonSearch(search)
}
