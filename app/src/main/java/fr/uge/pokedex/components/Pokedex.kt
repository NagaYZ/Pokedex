package fr.uge.pokedex.components




import android.content.Context
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import fr.uge.pokedex.data.Pokemon
import fr.uge.pokedex.data.PokemonRepository
import fr.uge.pokedex.ui.theme.PokedexTheme


@Composable
fun DisplayPokedex(context: Context, pokemons: List<Pokemon>) {
    LazyVerticalGrid(columns = GridCells.Fixed(1), horizontalArrangement = Arrangement.spacedBy(40.dp), verticalArrangement = Arrangement.spacedBy(50.dp), contentPadding = PaddingValues(30.dp, 30.dp)) {
        items(pokemons) {
            PokemonListDisplay(pokemon = it, context = context)


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


