package fr.uge.pokedex.service

import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.runtime.*
import androidx.compose.ui.res.painterResource
import fr.uge.pokedex.R

@Composable
fun MusicButton(musicService: PokemonMusicService?) {
    var isPlaying by remember { mutableStateOf(true) }

    Button(
        onClick = {
            if (isPlaying) {
                musicService!!.getMediaPlayer().pause()
            } else {
                musicService!!.getMediaPlayer().start()
            }
            isPlaying = !isPlaying
        }
    ) {
        if (isPlaying) {
            Icon(painter = painterResource(R.drawable.icon_audio), "Pause Audio Button")
        } else {
            Icon(painter = painterResource(R.drawable.icon_no_audio), "Resume Audio Button")
        }
    }

}