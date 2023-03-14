package fr.uge.pokedex.components.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import fr.uge.pokedex.components.navigation.Route
import fr.uge.pokedex.data.user.Profile
import fr.uge.pokedex.theme.Shapes

@Composable
fun ProfileItem(
    profile: Profile,
    navController: NavHostController,
    onDeleteProfile: (profile: Profile) -> Unit,
    onEditProfile: (profile: Profile) -> Unit,
    setCurrentProfile: (profile: Profile) -> Unit
) {
    Row(modifier = Modifier
        .width(300.dp)
        .height(50.dp)
        .background(MaterialTheme.colors.secondary, Shapes.medium), verticalAlignment = Alignment.CenterVertically) {

        Row(horizontalArrangement = Arrangement.Start, modifier = Modifier.weight(2.0f).clickable(onClick = {
            setCurrentProfile.invoke(profile)
            navController.navigate(Route.Pokedex.path)
        })){
            Text(text = profile.getProfileName(), style = MaterialTheme.typography.h6, modifier = Modifier.padding(start = 20.dp))
        }
        
        Row(modifier = Modifier.weight(1.0f), horizontalArrangement = Arrangement.End) {

            Icon(Icons.Rounded.Delete, "Delete profile", modifier = Modifier.scale(1.5f).padding(end = 20.dp).size(ButtonDefaults.IconSize).clickable(
                onClick = {onDeleteProfile.invoke(profile)}
            ))

            Icon(Icons.Rounded.Edit, "Edit profile", modifier = Modifier.scale(1.5f).padding(end = 20.dp).size(ButtonDefaults.IconSize).clickable(
                onClick = {onEditProfile.invoke(profile)}
            ))

        }
    }
}