package fr.uge.pokedex.components



import android.content.ContentValues.TAG
import android.content.Context
import android.content.res.Resources
import android.util.DisplayMetrics
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




    LazyVerticalGrid(columns = GridCells.Fixed(2), horizontalArrangement = Arrangement.spacedBy(40.dp), verticalArrangement = Arrangement.spacedBy(50.dp), contentPadding = PaddingValues(30.dp, 30.dp)) {
        items(pokemons) {
            PokemonBoxDisplay(pokemon = it, context = context)


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


