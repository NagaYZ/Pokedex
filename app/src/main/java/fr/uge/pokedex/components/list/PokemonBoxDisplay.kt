package fr.uge.pokedex.components.list

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import fr.uge.pokedex.data.pokedex.pokemon.Pokemon
import fr.uge.pokedex.data.user.Favorite

@Composable
fun PokemonBoxDisplay(
    pokemon: Pokemon,
    onClick: () -> Unit = {},
    onClickFavorite: (Long, Favorite?) -> Unit = { pokemonId:Long, favorite: Favorite? ->},
    favoriteList: List<Favorite>
) {

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .width(180.dp)
            .background(MaterialTheme.colors.background)
            .padding(25.dp),
        verticalArrangement = Arrangement.SpaceBetween,
    ) {

        PokemonSprite(spriteResource = pokemon.sprite)

        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Bottom,
            modifier = Modifier
                .padding(vertical = 4.dp)
                .fillMaxWidth()
        ) {

            PokemonBoxTitle(name = pokemon.name)
            Spacer(modifier = Modifier.width(3.dp))
            Text(
                text = "#${pokemon.id.toString().padStart(3, '0')}",
                color = Color.LightGray,
                fontStyle = FontStyle.Italic,
                textAlign = TextAlign.Center
            )

            var favorite:Favorite? = null
            for(f in favoriteList){
                if(f.getPokemonId() == pokemon.id) favorite = f
            }
            FavoriteButton(pokemonId = pokemon.id, favorite = favorite, onClick = onClickFavorite)
        }
        PokemonTypeDisplay(type = pokemon.type)
    }

}