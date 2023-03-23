package fr.uge.pokedex.service.music

import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import fr.uge.pokedex.R

@Composable
fun MusicButton(audioState: Boolean, onClick: () -> Unit) {

    Button(
        onClick = {
            onClick()
        },
        elevation = null
    ) {
        if (audioState) {
            Icon(painter = painterResource(R.drawable.icon_audio), "Pause Audio Button")
        } else {
            Icon(painter = painterResource(R.drawable.icon_no_audio), "Resume Audio Button")
        }
    }

}