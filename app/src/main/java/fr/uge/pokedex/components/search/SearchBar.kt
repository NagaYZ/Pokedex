package fr.uge.pokedex.components.search

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import fr.uge.pokedex.R


@Preview
@Composable
fun SearchBar(pokemonSearch: (String) -> Unit = {}) {
    var search by remember {
        mutableStateOf("")
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(5.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = painterResource(id = R.drawable.pok),
            contentDescription = "Search",
            modifier = Modifier
                .scale(1f)
                .height(30.dp)
                .padding(horizontal = 10.dp),
            tint = Color.Unspecified
        )

        OutlinedTextField(
            modifier = Modifier
                .padding(5.dp)
                .fillMaxWidth(),
            value = search,
            onValueChange = { search = it },
            placeholder = { Text("Search by name") }
        )
        pokemonSearch(search)
    }


    pokemonSearch(search)
}
