package fr.uge.pokedex.components.list

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.unit.dp

@Composable
fun PokemonSprite(spriteResource: Int) {
    Image(
        bitmap = ImageBitmap.imageResource(id = spriteResource),
        contentDescription = "Pokemon sprite",
        modifier = Modifier
            .background(Color.White)
            .aspectRatio(1f)
            .fillMaxWidth()
            .border(width = 1.dp, color = Color.LightGray),
        filterQuality = FilterQuality.None
    )
}