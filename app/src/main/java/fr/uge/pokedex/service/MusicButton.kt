package fr.uge.pokedex.service

import android.content.Context
import android.content.Intent
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.runtime.*
import androidx.compose.ui.res.painterResource
import fr.uge.pokedex.R

@Composable
fun MusicButton(context: Context, intent: Intent) {
    var isPlaying by remember { mutableStateOf(true) }
    Button(
        onClick = {
            if (isPlaying) {
                context.stopService(intent)
            } else {
                context.startService(intent)
            }
            isPlaying = !isPlaying
        }
    ) {
        Icon(painter = painterResource(R.drawable.icon_audio), "Audio Button")

    }
}