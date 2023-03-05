package fr.uge.pokedex.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import fr.uge.pokedex.R


@Composable
fun SearchBar(pokemonSearch: (String) -> Unit) {
    var search by remember {
        mutableStateOf("")
    }

    Row() {
        Icon(painter = painterResource(id = R.drawable.pok), contentDescription = "Recherche", modifier = Modifier.padding(5.dp, 17.dp).size(30.dp), tint = Color.Unspecified )

        OutlinedTextField(
            modifier = Modifier.padding(5.dp).fillMaxWidth(),
            value = search,
            onValueChange = { search = it },
            placeholder = { Text("Recherche par nom") }
        )
        pokemonSearch(search)
    }


}
