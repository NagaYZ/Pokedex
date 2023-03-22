package fr.uge.pokedex.components.pokedex


import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.Divider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import fr.uge.pokedex.components.list.PokemonListDisplay
import fr.uge.pokedex.data.pokedex.pokemon.Pokemon
import fr.uge.pokedex.data.user.Favorite

@Composable
fun PokedexDisplay(
    sizeGrid: Int = 1,
    pokemonList: List<Pokemon>,
    favoriteList: List<Favorite>,
    clickFavorite : (Long, Favorite?) -> Unit,
    onClick: (Long) -> Unit
)  {
    LazyVerticalGrid(
        columns = GridCells.Fixed(sizeGrid),
        verticalArrangement = Arrangement.spacedBy(5.dp)
    ) {
        items(items = pokemonList, key = {it.id}) { pokemon ->
            PokemonListDisplay(pokemon = pokemon, onClick = {
                onClick(pokemon.id)
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




