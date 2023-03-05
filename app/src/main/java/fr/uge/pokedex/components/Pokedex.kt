package fr.uge.pokedex.components




import android.content.Context
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import fr.uge.pokedex.data.Pokemon
import fr.uge.pokedex.database.Favorite
import fr.uge.pokedex.database.Profile


@Composable
fun DisplayPokedex(sizeGrid: Int = 1, context: Context, pokemons: List<Pokemon>, navController: NavHostController, favorites: List<Favorite>, profile: Profile, getPokemonId: (Long) -> Unit, getPokemonFavoriteId: (Long) -> Unit, clickFavorite : () -> Unit)  {

    LazyVerticalGrid(columns = GridCells.Fixed(sizeGrid), horizontalArrangement = Arrangement.spacedBy(40.dp), verticalArrangement = Arrangement.spacedBy(50.dp), contentPadding = PaddingValues(20.dp)) {
        items(pokemons) {
            favorites.forEach { fav -> if(fav.getPokemonId() == it.id && fav.getProfileId() == profile.getId()) it.isFavorite = true }
            PokemonListDisplay(pokemon = it, context = context, onClick = {
                navController.navigate("card")
                getPokemonId(it.id)
            }, onClickFavorite = {
                getPokemonFavoriteId(it.id)
                clickFavorite()
            })
        }
    }
}




