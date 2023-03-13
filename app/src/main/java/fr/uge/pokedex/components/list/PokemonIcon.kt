package fr.uge.pokedex.components.list

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.res.imageResource

@Composable
fun PokemonIcon(iconResource: Int, modifier: Modifier = Modifier) {
    Image(
        bitmap = ImageBitmap.imageResource(id = iconResource),
        contentDescription = "Pokemon icon",
        modifier = modifier
            .aspectRatio(1f)
            .fillMaxHeight()
    )
}