package fr.uge.pokedex.components

import android.annotation.SuppressLint
import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.test.platform.app.InstrumentationRegistry
import fr.uge.pokedex.data.Pokemon
import fr.uge.pokedex.database.*
import fr.uge.pokedex.ui.theme.Purple200
import fr.uge.pokedex.ui.theme.Purple500

@Composable
private fun DeleteTeam(teamId: Long) {
    PokedexAppDatabaseConnection.initialise(InstrumentationRegistry.getInstrumentation().targetContext)
    val teamDao: TeamDao = PokedexAppDatabaseConnection.connection.teamDao()
    val team: Team = teamDao.getTeam(teamId)
    teamDao.deleteTeam(team)
}

@Composable
private fun getTeams(profile: Profile): List<TeamWithMembers> {
    PokedexAppDatabaseConnection.initialise(InstrumentationRegistry.getInstrumentation().targetContext)
    val profileDao: ProfileDao = PokedexAppDatabaseConnection.connection.profileDao()

    return profileDao.getProfileWithTeam(profile.getId()).teamsWithMembers
}
/*

@Composable
fun EditTeam( teamss : Team,
    show: Boolean,
              pokemons: Map<Long, Pokemon>,
              context: Context,
              favorites: List<Favorite>,
              profile: Profile,
              close: () -> Unit) {
    var createTeam by remember { mutableStateOf(false) }
    val team by remember {
        mutableStateOf(mutableListOf<Long>())
    }
    var pickedPokemon by remember { mutableStateOf(-1L) }

    if (!show) return
    AlertDialog(onDismissRequest = { close() },
        title = { Text("Team creation") },
        backgroundColor = Purple500,
        text = {
            Column(
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            ) {
                for (i in 1..6) {
                    PickPokemon(pokemons, context, favorites, profile, getPokemonId = {
                        pickedPokemon = it
                    })
                    if (pickedPokemon != -1L) {
                        team.add(pickedPokemon)
                    }
                    pickedPokemon = -1L
                }
            }
        },
        confirmButton = {
            Row(
                modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center
            ) {
                Button(onClick = { close(); createTeam = true }) {
                    Text("Done")
                }
            }
        })
    if (createTeam) {
        AddTeamToDatabase(team, profile)
    }

    //show pokedex to select pokemon
    if (showPokemonList) {
        PokedexDisplay(pokemons, context, favorites, profile, getPokemonId) {
            showPokemonList = false
        }

    }
}
*/

@Composable
fun DisplayTeams(pokemons: Map<Long, Pokemon>, context: Context, profile: Profile) {
    var showNewTeamDialog by remember { mutableStateOf(false) }
    var delete by remember { mutableStateOf(false) }
    var edit by remember { mutableStateOf(false) }
    var teamId by remember { mutableStateOf(-1L) }
    var teams = getTeams(profile = profile)

    //display list of team
    LazyVerticalGrid(
        columns = GridCells.Fixed(1),
        horizontalArrangement = Arrangement.spacedBy(40.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp),
        contentPadding = PaddingValues(30.dp, 35.dp)
    ) {
        items(teams) { poketeam ->
            TeamDisplay(
                pokemon_team = poketeam,
                context = context,
                { teamId = it; edit = true },
                { teamId = it; delete = true }) {}
        }
    }

    //Button : Add New Team
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Bottom,
        horizontalAlignment = Alignment.End
    ) {
        Button(modifier = Modifier.padding(38.dp), onClick = {
            showNewTeamDialog = true
        }) {
            Icon(Icons.Rounded.Add, "Add Team")
        }
    }


    if (delete) {
        DeleteTeam(teamId)
        delete = false
    }
    if (edit) {
        // EditTeam(teamId)
        edit = false
    }


    if (showNewTeamDialog) {
        PopupWindow(
            show = showNewTeamDialog,
            pokemons,
            context,
            profile
        ) { showNewTeamDialog = false }
    }
}


@SuppressLint("MutableCollectionMutableState")
@Composable
fun PopupWindow(
    show: Boolean,
    pokemons: Map<Long, Pokemon>,
    context: Context,
    profile: Profile,
    close: () -> Unit
) {
    var createTeam by remember { mutableStateOf(false) }
    val team by remember {
        mutableStateOf(mutableListOf<Long>())
    }
    var pickedPokemon by remember { mutableStateOf(-1L) }

    if (!show) return
    AlertDialog(onDismissRequest = { close() },
        title = { Text("Team creation") },
        backgroundColor = Purple500,
        text = {
            Column(
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            ) {
                for (i in 1..6) {
                    PickPokemon(pokemons, context, profile, getPokemonId = {
                        pickedPokemon = it
                    })
                    if (pickedPokemon != -1L) {
                        team.add(pickedPokemon)
                    }
                    pickedPokemon = -1L
                }
            }
        },
        confirmButton = {
            Row(
                modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center
            ) {
                Button(onClick = { close(); createTeam = true }) {
                    Text("Done")
                }
            }
        })
    if (createTeam) {
        AddTeamToDatabase(team, profile)
    }

}

@Composable
fun AddTeamToDatabase(team: List<Long>, profile: Profile) {
    PokedexAppDatabaseConnection.initialise(InstrumentationRegistry.getInstrumentation().targetContext)
    val teamDao: TeamDao = PokedexAppDatabaseConnection.connection.teamDao()
    val teamMemberDao: TeamMemberDao = PokedexAppDatabaseConnection.connection.teamMemberDao()
    val teamId: Long = teamDao.addTeam(Team("Team de " + profile.getProfileName(), profile.getId()))

    team.forEach { pokemon ->
        teamMemberDao.addTeamMember(TeamMember(pokemon, teamId))
    }
}

@Composable
fun PickPokemon(
    pokemons: Map<Long, Pokemon>,
    context: Context,
    profile: Profile,
    getPokemonId: (Long) -> Unit
) {
    var showPokemonList by remember { mutableStateOf(false) }
    val currentPokemon by remember { mutableStateOf(-1L) }
    val copyPokemons by remember { mutableStateOf(pokemons) }

    Box(
        modifier = Modifier
            .padding(4.dp)
            .height(70.dp)
            .fillMaxWidth()
            .background(Purple200, RoundedCornerShape(8.dp))
            .clickable {
                showPokemonList = true
            },
    ) {
        Text("Choose Pokemon...", fontSize = 20.sp)
        copyPokemons.get(currentPokemon)?.let {
            PokemonTeamDisplay(it, context) {}
            getPokemonId(it.id)
        }
        Button(
            onClick = { /*TODO*/ },
            modifier = Modifier
                .align(Alignment.TopEnd)
                .size(40.dp)
        ) {
            Icon(
                Icons.Rounded.Delete,
                contentDescription = "Delete Selected Pokemon",
                Modifier.scale(3f)
            )
        }
    }
    //show pokedex to select pokemon
    if (showPokemonList) {
        PokedexDisplay(pokemons, context, profile, getPokemonId) {
            showPokemonList = false
        }
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun PokedexDisplay(
    pokemons: Map<Long, Pokemon>,
    context: Context,
    profile: Profile,
    getPokemonId: (Long) -> Unit,
    onClick: () -> Unit
) {

    var resultList by remember {
        mutableStateOf(mutableListOf<Pokemon>())
    }
    var fav by remember {
        mutableStateOf(Favorite(-1L, -1L))
    }
    var currentIconFavorite by remember {
        mutableStateOf(-1L)
    }

    var favorites by remember {
        mutableStateOf(
            PokedexAppDatabaseConnection.connection.profileDao()
                .getProfileWithFavorites(profile.getId()).favorites
        )
    }
    Dialog(
        onDismissRequest = { onClick() }, properties = DialogProperties(
            dismissOnBackPress = true, usePlatformDefaultWidth = false
        )
    ) {
        Column(
            modifier = Modifier
                .background(MaterialTheme.colors.background)
                .fillMaxSize()
        ) {
            FiltersBar(pokemons.values.toList()) {
                resultList = it.toMutableList()
            }

            DisplayPokedex(
                context = context,
                pokemons = resultList,
                profile = profile,
                getPokemonId = getPokemonId,
                getPokemonFavoriteId = {
                    currentIconFavorite = it
                },
                clickFavorite = {
                    println(it)
                    if (!it) {
                        println("deleted 1")
                        favorites.forEach { favorite ->
                            println("deleted 2")
                            if (favorite.getPokemonId() == currentIconFavorite) {
                                println("deleted")
                                PokedexAppDatabaseConnection.connection.favoriteDao()
                                    .deleteFavorite(favorite)
                                favorites = PokedexAppDatabaseConnection.connection.profileDao()
                                    .getProfileWithFavorites(profile.getId()).favorites
                            }
                        }
                    } else {
                        fav = Favorite(currentIconFavorite, profile.getId())
                        if (!favorites.contains(fav)) {
                            PokedexAppDatabaseConnection.connection.favoriteDao().addFavorite(fav)
                            favorites = PokedexAppDatabaseConnection.connection.profileDao()
                                .getProfileWithFavorites(profile.getId()).favorites
                        }
                    }
                },
                onClick = { onClick() })
        }
    }

}

@Preview
@Composable
fun Dialog_preview() {

    AlertDialog(onDismissRequest = { }, title = { Text("Team creation") }, text = {
        Column(
            Modifier.fillMaxWidth()
        ) {
            repeat(6) {
                Row() {
                    Box(
                        modifier = Modifier
                            .padding(4.dp)
                            .height(70.dp)
                            .fillMaxWidth()
                            .background(Color.Gray, RoundedCornerShape(8.dp))
                    ) {
                        Button(
                            onClick = { /*TODO*/ },
                            modifier = Modifier
                                .align(Alignment.TopEnd)
                                .size(40.dp)
                        ) {
                            Icon(
                                Icons.Rounded.Delete,
                                contentDescription = "Delete Selected Pokemon",
                                Modifier.scale(3f)
                            )
                        }
                    }
                }

            }
        }

    }, confirmButton = {
        Button(onClick = { }) {
            Text("Done")
        }
    })
}



