package fr.uge.pokedex.components.profile

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import fr.uge.pokedex.data.user.PokedexAppDatabaseConnection
import fr.uge.pokedex.data.user.Profile


@Composable
fun ProfilesScreen(
    navController: NavHostController,
    setCurrentProfile: (profile: Profile) -> Unit
) {

    val profileDao = PokedexAppDatabaseConnection.connection.profileDao()

    var showNewProfileDialog by remember { mutableStateOf(false) }
    var showEditProfileDialog by remember { mutableStateOf(false) }
    var profilesList by remember { mutableStateOf(profileDao.getAllProfiles()) }

    var profileByRememberToEdit: Profile by remember { mutableStateOf(Profile("")) }

    if (profilesList.isEmpty()) {

        //User enter the app for the first time so we need to propose him to create a new profile
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.SpaceEvenly,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
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
    } else {

        Text(text = "Profiles", style = MaterialTheme.typography.h3)

        //Profile list
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            profilesList.forEach { profile: Profile ->
                ProfileItem(profile = profile, navController = navController,
                    onDeleteProfile = { profileToDelete: Profile ->
                        profileDao.deleteProfile(profileToDelete)
                        profilesList = profileDao.getAllProfiles()
                    }, onEditProfile = { profileToEdit: Profile ->
                        profileByRememberToEdit = profileToEdit
                        showEditProfileDialog = true
                    }, setCurrentProfile
                )
            }
        }

        //Add new profile button
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Bottom,
            horizontalAlignment = Alignment.End
        ) {
            Button(modifier = Modifier.padding(20.dp), onClick = {
                showNewProfileDialog = true
            }) {
                Icon(Icons.Rounded.Add, "Add profile")
            }
        }
    }

    //Dialog when the user needs to create a new profile
    ProfileDialog(
        show = showNewProfileDialog,
        close = { showNewProfileDialog = false },
        onProfileNameAccept = { profileName ->
            profileDao.addProfile(Profile(profileName))
            profilesList = profileDao.getAllProfiles()
        })

    //Dialog when the user needs to edit a profile
    ProfileDialog(
        show = showEditProfileDialog,
        close = { showEditProfileDialog = false },
        onProfileNameAccept = { profileName ->
            profileByRememberToEdit.setProfileName(profileName)
            profileDao.updateProfile(profileByRememberToEdit)
            profilesList = profileDao.getAllProfiles()
        })

}

