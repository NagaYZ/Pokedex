package fr.uge.pokedex.components


import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.Divider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import fr.uge.pokedex.data.Pokemon
import fr.uge.pokedex.database.PokedexAppDatabaseConnection
import fr.uge.pokedex.database.Profile


@Composable
fun DisplayPokedex(
    sizeGrid: Int = 1,
    pokemonList: List<Pokemon>,
    profile: Profile,
    getPokemonId: (Long) -> Unit,
    getPokemonFavoriteId: (Long) -> Unit,
    clickFavorite : (Boolean) -> Unit,
    onClick: () -> Unit
)  {

    val favoriteList by remember {
        mutableStateOf(
            PokedexAppDatabaseConnection.connection.profileDao()
                .getProfileWithFavorites(profile.getId()).favorites.map { it.getPokemonId() })
    }
    LazyVerticalGrid(
        columns = GridCells.Fixed(sizeGrid),
        verticalArrangement = Arrangement.spacedBy(5.dp)
    ) {
        items(pokemonList) { pokemon ->
            PokemonListDisplay(pokemon = pokemon, onClick = {
                onClick()
                getPokemonId(pokemon.id)
            }, onClickFavorite = {
                getPokemonFavoriteId(pokemon.id)
                clickFavorite(it)
            }, favoriteList = favoriteList)
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




