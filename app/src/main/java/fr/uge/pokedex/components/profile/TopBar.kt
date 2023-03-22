package fr.uge.pokedex.components.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import fr.uge.pokedex.components.navigation.Route
import fr.uge.pokedex.data.user.PokedexAppDatabase
import fr.uge.pokedex.service.MusicButton
import kotlinx.coroutines.runBlocking

@Composable
fun TopBar(
    navController: NavHostController,
    currentProfileId: Long,
    onClick: () -> Unit,
    audioState: Boolean
) {
    val context = LocalContext.current
    val profile = runBlocking {
        PokedexAppDatabase.getConnection(context).profileDao().getProfile(currentProfileId)
    }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(40.dp)
            .background(color = MaterialTheme.colors.primary),
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceEvenly,
            modifier = Modifier.clickable {
                navController.navigate(route = Route.Profiles.path)
            }) {
            Icon(
                Icons.Rounded.ArrowBack,
                "Back to profiles",
                modifier = Modifier.padding(12.dp),
                tint = Color.White
            )
        }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceEvenly,

            ) {
            Text(
                text = profile.getProfileName(),
                style = MaterialTheme.typography.button,
                modifier = Modifier.padding(12.dp),
                color = Color.White
            )
        }

        Column(
            horizontalAlignment = Alignment.End,
            modifier = Modifier.weight(1f)
        ) {
            MusicButton(audioState, onClick)
        }
    }
}