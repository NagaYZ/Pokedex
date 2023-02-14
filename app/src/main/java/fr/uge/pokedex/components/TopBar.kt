package fr.uge.pokedex.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import fr.uge.pokedex.data.ProfilesService

@Composable
fun TopBar(navController: NavHostController){

    val currentProfile by remember { mutableStateOf(ProfilesService.getCurrentProfile()) }

    Row(modifier = Modifier
        .fillMaxWidth()
        .height(40.dp)
        .background(color = MaterialTheme.colors.primary), horizontalArrangement = Arrangement.Start, verticalAlignment = Alignment.CenterVertically) {


        Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.SpaceEvenly, modifier = Modifier.clickable {
            navController.navigate(route = Route.Profiles.path)
        }) {
            Icon(Icons.Rounded.ArrowBack, "Back to profiles", modifier = Modifier.padding(12.dp))
        }

        Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.SpaceEvenly) {
            currentProfile?.let { Text(text = it.getProfileName(), style = MaterialTheme.typography.button, modifier = Modifier.padding(12.dp)) }
        }
    }
}