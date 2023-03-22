package fr.uge.pokedex.components.profile

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import fr.uge.pokedex.R
import fr.uge.pokedex.data.user.PokedexAppDatabase
import fr.uge.pokedex.data.user.Profile
import fr.uge.pokedex.service.MusicButton
import fr.uge.pokedex.service.PokemonMusicService
import kotlinx.coroutines.runBlocking

@Composable
fun ProfilesScreen(
    navController: NavHostController,
    setCurrentProfile: (profileId: Long) -> Unit,
    pokemonMusicService: PokemonMusicService
) {

    val profileDao = PokedexAppDatabase.getConnection(LocalContext.current).profileDao()

    var showNewProfileDialog by remember { mutableStateOf(false) }
    var showEditProfileDialog by remember { mutableStateOf(false) }
    var profilesList by remember { mutableStateOf(runBlocking { profileDao.getAllProfiles() }) }

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
                UgePokedexLogo()
                Text(text = "Create a new profile", style = MaterialTheme.typography.h5)
                Button(onClick = {
                    showNewProfileDialog = !showNewProfileDialog
                }) {
                    Text(text = "New profile", style = MaterialTheme.typography.button)
                }
            }
        }
    } else {

        Column {

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                UgePokedexLogo()
            }
            Column(Modifier.weight(2f), horizontalAlignment = Alignment.CenterHorizontally) {

                Row {
                    Icon(
                        painter = painterResource(id = R.drawable.pok),
                        contentDescription = "Ball",
                        modifier = Modifier
                            .scale(1f)
                            .height(25.dp)
                            .padding(horizontal = 10.dp),
                        tint = Color.Unspecified
                    )
                    Text(text = "Profiles", style = MaterialTheme.typography.h6)
                    Icon(
                        painter = painterResource(id = R.drawable.pok),
                        contentDescription = "Ball",
                        modifier = Modifier
                            .scale(1f)
                            .height(25.dp)
                            .padding(horizontal = 10.dp),
                        tint = Color.Unspecified
                    )
                }

                Spacer(Modifier.padding(top = 5.dp, bottom = 5.dp))

                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Top
                ) {
                    items(items = profilesList, key = { it.getId() }) { profile ->

                        ProfileItem(profile = profile, navController = navController,
                            onDeleteProfile = { profileToDelete: Profile ->
                                runBlocking { profileDao.deleteProfile(profileToDelete) }
                                profilesList = runBlocking { profileDao.getAllProfiles() }
                            }, onEditProfile = { profileToEdit: Profile ->
                                profileByRememberToEdit = profileToEdit
                                showEditProfileDialog = true
                            }, setCurrentProfile
                        )

                        Spacer(Modifier.padding(top = 5.dp, bottom = 5.dp))
                    }
                }

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

        //Audio control button
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Bottom,
            horizontalAlignment = Alignment.Start
        ) {
            Box(modifier = Modifier.padding(20.dp)) {
                MusicButton(musicService = pokemonMusicService)
            }
        }
    }

    //Dialog when the user needs to create a new profile
    ProfileDialog(
        show = showNewProfileDialog,
        close = { showNewProfileDialog = false },
        onProfileNameAccept = { profileName ->
            runBlocking { profileDao.addProfile(Profile(profileName)) }
            profilesList = runBlocking { profileDao.getAllProfiles() }
        })

    //Dialog when the user needs to edit a profile
    ProfileDialog(
        show = showEditProfileDialog,
        close = { showEditProfileDialog = false },
        onProfileNameAccept = { profileName ->
            profileByRememberToEdit.setProfileName(profileName)
            runBlocking { profileDao.updateProfile(profileByRememberToEdit) }
            profilesList = runBlocking { profileDao.getAllProfiles() }
        })

}

@Composable
fun UgePokedexLogo() {
    Row() {
        Icon(
            painter = painterResource(id = R.drawable.icon_pkm_25),
            contentDescription = "Ball",
            modifier = Modifier
                .scale(5f)
                .height(50.dp)
                .padding(horizontal = 10.dp),
            tint = Color.Unspecified
        )
        Spacer(Modifier.padding(end = 10.dp))
        Text(
            text = "UGE Pokedex",
            style = MaterialTheme.typography.h3,
            color = MaterialTheme.colors.primary,
            fontWeight = FontWeight.ExtraBold
        )
        Spacer(Modifier.padding(start = 10.dp))
        Icon(
            painter = painterResource(id = R.drawable.icon_pkm_151),
            contentDescription = "Ball",
            modifier = Modifier
                .scale(5f)
                .height(60.dp)
                .padding(horizontal = 10.dp),
            tint = Color.Unspecified,
        )
    }
}

