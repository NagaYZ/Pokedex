package fr.uge.pokedex.components.pokedex


import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.Divider
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import fr.uge.pokedex.components.list.PokemonListDisplay
import fr.uge.pokedex.data.pokedex.Pokemon
import fr.uge.pokedex.data.user.Favorite
import fr.uge.pokedex.data.user.PokedexAppDatabase
import fr.uge.pokedex.data.user.Profile
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.runBlocking


@Composable
fun PokedexDisplay(
    sizeGrid: Int = 1,
    pokemonList: List<Pokemon>,
    profile: Profile,
    clickFavorite : (Long, Favorite?) -> Unit,
    onClick: (Long) -> Unit
)  {
    val context = LocalContext.current
    var favoriteList = runBlocking { PokedexAppDatabase.getConnection(context).profileDao().getProfileWithFavorites(profile.getId()).favorites }

    LazyVerticalGrid(
        columns = GridCells.Fixed(sizeGrid),
        verticalArrangement = Arrangement.spacedBy(5.dp)
    ) {
        items(items = pokemonList, key = {it.id}) { pokemon ->
            PokemonListDisplay(pokemon = pokemon, onClick = {
                onClick(pokemon.id)
//                getPokemonId(pokemon.id)
            }, onClickFavorite = clickFavorite,
                favoriteList = favoriteList)
            Divider(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 10.dp)
            )
        }
        item {
            Spacer(modifier = Modifier.height(100.dp))
        }
    }
}




