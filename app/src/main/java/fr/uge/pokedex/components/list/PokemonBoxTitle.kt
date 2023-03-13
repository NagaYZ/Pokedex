package fr.uge.pokedex.components.list

import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun PokemonBoxTitle(name: String, modifier: Modifier = Modifier) {
    Text(
        text = name,
        fontSize = 18.sp,
        modifier = modifier.padding(horizontal = 3.dp),
        style = MaterialTheme.typography.h3.copy(
            shadow = Shadow(
                color = Color.LightGray,
                offset = Offset(x = 0f, y = 3f),
                blurRadius = 0.1f
            )
        )
    )
}