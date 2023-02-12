package fr.uge.pokedex.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavHostController
import fr.uge.pokedex.data.Profile
import fr.uge.pokedex.data.ProfilesService


@Composable
fun ProfilesScreen(navController: NavHostController){

    var showNewProfileDialog by remember {mutableStateOf(false)}
    var showEditProfileDialog by remember {mutableStateOf(false)}
    var profilesArray by remember { mutableStateOf(ProfilesService.getProfiles()) }

    var profileToEdit: Profile by remember { mutableStateOf(Profile("")) }

        if(profilesArray.isEmpty()){
            Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.SpaceEvenly, horizontalAlignment = Alignment.CenterHorizontally) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = "Create a new profile", style = MaterialTheme.typography.h5)
                    Button(onClick = {
                        showNewProfileDialog = !showNewProfileDialog
                    }) {
                        Text(text = "New profile", style = MaterialTheme.typography.button)
                    }
                }
            }
        }
        else{
            Text(text = "Profiles", style = MaterialTheme.typography.h3)

            Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
                profilesArray.forEach{profile: Profile ->
                    ProfileItem(profile = profile, navController = navController,
                        onDeleteProfile = {profile: Profile ->
                            ProfilesService.deleteProfile(profile)
                            profilesArray = ProfilesService.getProfiles()
                    }, onEditProfile = {profile: Profile ->
                            profileToEdit = profile
                            showEditProfileDialog = true
                    })
                }
            }
        }

        NewProfileDialog(show = showNewProfileDialog, close = {showNewProfileDialog = false}, onProfileNameAccept = { profileName ->
            ProfilesService.addProfile(Profile(profileName))
            profilesArray = ProfilesService.getProfiles()
        })

        NewProfileDialog(show = showEditProfileDialog, close = {showEditProfileDialog = false}, onProfileNameAccept = { profileName ->
            profileToEdit?.let { ProfilesService.editProfile(it, profileName) }
            profilesArray = ProfilesService.getProfiles()
        })

}

@Composable
fun ProfileItem(profile: Profile, navController: NavHostController, onDeleteProfile :(profile: Profile) -> Unit, onEditProfile :(profile: Profile) -> Unit){
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {

        Button(modifier = Modifier.padding(3.dp), onClick = {
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

@Composable
fun NewProfileDialog(show :Boolean, close :() -> Unit, onProfileNameAccept :(profileName:String) -> Unit){
    if (!show) return

    Dialog(onDismissRequest = { close.invoke()}) {

        Column(modifier = Modifier.background(MaterialTheme.colors.background), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.SpaceBetween){
            var profileName by remember { mutableStateOf("") }

            Text(text = "Please enter new profile name", style = MaterialTheme.typography.h6)
            TextField(value = profileName, onValueChange = {
                profileName = it
            })
            Button(onClick = {
                onProfileNameAccept.invoke(profileName)
                close.invoke()
            }) {
                Text(text = "Confirm", style = MaterialTheme.typography.button)
            }
        }

    }
}