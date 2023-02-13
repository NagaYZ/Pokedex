package fr.uge.pokedex.components



import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

import fr.uge.pokedex.data.Pokemon
import fr.uge.pokedex.data.PokemonRepository
import fr.uge.pokedex.ui.theme.PokedexTheme

@Composable
fun DisplayPokedex(context: Context, pokemons: List<Pokemon>) {
    var clicked by rememberSaveable { mutableStateOf(false) }

    LazyVerticalGrid(columns = GridCells.Adaptive(100.dp), horizontalArrangement = Arrangement.spacedBy(10.dp), verticalArrangement = Arrangement.spacedBy(10.dp), contentPadding = PaddingValues(50.dp, 50.dp)) {
        items(pokemons) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .border(2.dp, Color.Black)
                    .clickable {
                        if (!clicked) {
                            clicked = true
                        }
                    }, horizontalAlignment = Alignment.CenterHorizontally,
            ) {

                Image(
                    painter = painterResource(it.getIcon(context)),
                    contentDescription = it.name,
                    modifier = Modifier
                        .width(100.dp)
                        .height(100.dp)
                )

                Text(text = "N°" + String.format("%03d", it.id) + " " + it.name)
                Text(text = it.type.first.toString() + if (it.type.second.toString() == "NONE") "" else " - " + it.type.second.toString())

            }


        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    PokedexTheme {
        DisplayPokedex(
            pokemons = PokemonRepository(LocalContext.current).getAll().toList(),
            context = LocalContext.current
        )

    }
}


