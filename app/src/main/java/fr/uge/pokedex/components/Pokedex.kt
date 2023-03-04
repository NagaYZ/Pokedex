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
import fr.uge.pokedex.database.Favorite
import fr.uge.pokedex.database.FavoriteDao
import fr.uge.pokedex.database.Profile


@Composable
fun DisplayPokedex(context: Context, pokemons: List<Pokemon>, navController: NavHostController, favoriteData : FavoriteDao, profile : Profile, getPokemonId: (Long) -> Unit, getFavorite : (Favorite) -> Unit) {
    var fav by remember {
        mutableStateOf(Favorite(-1L, -1L))
    }
    LazyVerticalGrid(columns = GridCells.Fixed(1), horizontalArrangement = Arrangement.spacedBy(40.dp), verticalArrangement = Arrangement.spacedBy(50.dp), contentPadding = PaddingValues(30.dp, 30.dp)) {
        items(pokemons) {
            PokemonListDisplay(pokemon = it, context = context, {
                navController.navigate("card")
                getPokemonId(it.id)
            }, {
                navController.navigate("favorite")
                fav = Favorite(it.id, profile.getId())
                favoriteData.addFavorite(fav)
                getFavorite(fav)
            })
        }
    }
}




