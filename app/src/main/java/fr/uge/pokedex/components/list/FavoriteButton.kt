package fr.uge.pokedex.components.list

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.size
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import fr.uge.pokedex.data.user.Favorite

@Composable
fun FavoriteButton(pokemonId:Long, favorite: Favorite?, onClick: (Long, Favorite?) -> Unit) {
    var isClicked by remember { mutableStateOf(favorite != null) }

    Icon(
        imageVector = if (isClicked) Icons.Filled.Favorite else Icons.Default.FavoriteBorder,
        contentDescription = "Favorite",
        modifier = Modifier
            .size(ButtonDefaults.IconSize)
            .scale(1.25f)
            .clickable(onClick = {
                isClicked = !isClicked

                if(isClicked)
                    onClick.invoke(pokemonId, null)
                else
                    onClick.invoke(pokemonId, favorite)
            }
            ),
        tint = if (isClicked) Color(0xFFFF8686) else Color.LightGray,
    )
}