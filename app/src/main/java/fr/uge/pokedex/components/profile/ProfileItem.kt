package fr.uge.pokedex.components.profile

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import fr.uge.pokedex.components.navigation.Route
import fr.uge.pokedex.data.user.Profile

@Composable
fun ProfileItem(
    profile: Profile,
    navController: NavHostController,
    onDeleteProfile: (profile: Profile) -> Unit,
    onEditProfile: (profile: Profile) -> Unit,
    setCurrentProfile: (profile: Profile) -> Unit
) {
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {

        Button(modifier = Modifier.padding(3.dp), onClick = {
            setCurrentProfile.invoke(profile)
            navController.navigate(Route.Pokedex.path)
        }) {
            Text(text = profile.getProfileName(), style = MaterialTheme.typography.button)
        }

        Button(modifier = Modifier.padding(3.dp), onClick = {
            onDeleteProfile.invoke(profile)
        }) {
            Icon(Icons.Rounded.Delete, "Delete profile")
        }

        Button(modifier = Modifier.padding(3.dp), onClick = {
            onEditProfile.invoke(profile)
        }) {
            Icon(Icons.Rounded.Edit, "Edit profile")
        }
    }
}