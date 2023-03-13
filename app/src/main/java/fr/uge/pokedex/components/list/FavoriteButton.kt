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

@Composable
fun FavoriteButton(filled: Boolean, onClick: (Boolean) -> Unit) {
    var isClicked by remember { mutableStateOf(filled) }

    Icon(
        imageVector = if (isClicked) Icons.Filled.Favorite else Icons.Default.FavoriteBorder,
        contentDescription = "Favorite",
        modifier = Modifier
            .size(ButtonDefaults.IconSize)
            .scale(1.25f)
            .clickable(onClick = {
                isClicked = !isClicked
                onClick.invoke(isClicked)
            }
            ),
        tint = if (isClicked) Color(0xFFFF8686) else Color.LightGray,
    )
}