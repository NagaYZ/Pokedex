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
import fr.uge.pokedex.data.pokedex.Pokemon

@Composable
fun PokemonListDisplay(
    pokemon: Pokemon,
    onClick: () -> Unit,
    onClickFavorite: (Boolean) -> Unit,
    favoriteList: List<Long>
) {
    Row(
        Modifier
            .clickable(onClick = onClick)
            .background(MaterialTheme.colors.background)
            .height(70.dp)
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        PokemonIcon(pokemon.icon)
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .padding(horizontal = 8.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Bottom
            ) {
                PokemonBoxTitle(name = pokemon.name)
                Spacer(modifier = Modifier.width(3.dp))
                Text(
                    text = "#${pokemon.id.toString().padStart(3, '0')}",
                    color = Color.LightGray,
                    fontStyle = FontStyle.Italic,
                    textAlign = TextAlign.Center
                )
            }
            PokemonTypeDisplay(type = pokemon.type)
        }
        Row(
            modifier = Modifier.weight(1.0f),
            horizontalArrangement = Arrangement.End
        ) {
            FavoriteButton(filled = favoriteList.contains(pokemon.id), onClick = onClickFavorite)
            Spacer(modifier = Modifier.width(15.dp))
        }
    }
}