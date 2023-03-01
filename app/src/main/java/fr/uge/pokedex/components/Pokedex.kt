package fr.uge.pokedex.components




import android.content.Context
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.*
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController

import fr.uge.pokedex.data.Pokemon


@Composable
fun DisplayPokedex(context: Context, pokemons: List<Pokemon>, navController: NavHostController) {

    LazyVerticalGrid(columns = GridCells.Fixed(1), horizontalArrangement = Arrangement.spacedBy(40.dp), verticalArrangement = Arrangement.spacedBy(50.dp), contentPadding = PaddingValues(30.dp, 30.dp)) {
        items(pokemons) {
            PokemonListDisplay(pokemon = it, context = context, {
                navController.navigate("card")
            })
        }
    }
}




