package fr.uge.pokedex.components




import android.content.Context
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import fr.uge.pokedex.data.Pokemon
import fr.uge.pokedex.database.PokedexAppDatabaseConnection
import fr.uge.pokedex.database.Profile


@Composable
fun DisplayPokedex(sizeGrid: Int = 1, context: Context, pokemonList: List<Pokemon>, navController: NavHostController, profile: Profile, getPokemonId: (Long) -> Unit, getPokemonFavoriteId: (Long) -> Unit, clickFavorite : (Boolean) -> Unit)  {
    val favoriteList by remember {
        mutableStateOf(PokedexAppDatabaseConnection.connection.profileDao().getProfileWithFavorites(profile.getId()).favorites.map { it.getPokemonId() })
    }
    LazyVerticalGrid(columns = GridCells.Fixed(sizeGrid), horizontalArrangement = Arrangement.spacedBy(30.dp), verticalArrangement = Arrangement.spacedBy(20.dp), contentPadding = PaddingValues(10.dp)) {
        items(pokemonList) { pokemon ->
            PokemonListDisplay(pokemon = pokemon, context = context, onClick = {
                navController.navigate("card")
                getPokemonId(pokemon.id)
            }, onClickFavorite = {
                getPokemonFavoriteId(pokemon.id)
                clickFavorite(it)
            }, favoriteList = favoriteList)
        }
    }
}




