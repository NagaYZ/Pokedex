package fr.uge.pokedex.components

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
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
private fun getTeamsFromProfile(profile: Profile): List<TeamWithMembers> {
    PokedexAppDatabaseConnection.initialise(InstrumentationRegistry.getInstrumentation().targetContext)
    val profileDao: ProfileDao = PokedexAppDatabaseConnection.connection.profileDao()
    return profileDao.getProfileWithTeam(profile.getId()).teamsWithMembers
}

@Composable
private fun getPokemonsFromTeamId(teamId: Long): List<Long> {
    PokedexAppDatabaseConnection.initialise(InstrumentationRegistry.getInstrumentation().targetContext)
    val teamDao: TeamDao = PokedexAppDatabaseConnection.connection.teamDao()
    val teamWithMembers = teamDao.getTeamWithMembers(teamId)
    return teamWithMembers.teamMembers.map { member -> member.getPokemonId() }
}

@Composable
fun EditTeam(pokemonList: List<Long>, teamId: Long) {
    PokedexAppDatabaseConnection.initialise(InstrumentationRegistry.getInstrumentation().targetContext)
    val teamDao: TeamDao = PokedexAppDatabaseConnection.connection.teamDao()
    val teamMemberDao: TeamMemberDao = PokedexAppDatabaseConnection.connection.teamMemberDao()
    val teamWithMembers = teamDao.getTeamWithMembers(teamId)

    for (i in 0..5) {
        teamMemberDao.deleteTeamMember(teamWithMembers.teamMembers[i])
    }

    pokemonList.forEach { pokemon ->
        teamMemberDao.addTeamMember(TeamMember(pokemon, teamId))
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
fun DisplayTeams(
    pokemons: Map<Long, Pokemon>,
    profile: Profile,
    onPokemonClick: () -> Unit
) {
    var showNewTeamDialog by remember { mutableStateOf(false) }
    var delete by remember { mutableStateOf(false) }
    var edit by remember { mutableStateOf(false) }
    var teamId by remember { mutableStateOf(-1L) }
    val teams = getTeamsFromProfile(profile = profile)

    //display list of team
    LazyVerticalGrid(
        columns = GridCells.Fixed(1),
        horizontalArrangement = Arrangement.spacedBy(30.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp),
        contentPadding = PaddingValues(10.dp)
    ) {
        itemsIndexed(teams) { i, poketeam ->
            TeamDisplay(
                i + 1,
                pokemon_team = poketeam,
                { teamId = it; edit = true },
                { teamId = it; delete = true },
                onPokemonClick
            ) {}
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

    if (showNewTeamDialog) {
        PopupWindow(
            show = showNewTeamDialog,
            pokemons,
            profile,
            teamId,
            edit,
        ) { showNewTeamDialog = false }

        edit = false
    }
}

@SuppressLint("MutableCollectionMutableState")
@Composable
fun PopupWindow(
    show: Boolean,
    pokemons: Map<Long, Pokemon>,
    profile: Profile,
    teamId: Long,
    edit: Boolean,
    close: () -> Unit
) {
    var createTeam by remember { mutableStateOf(false) }
    val team by remember {
        mutableStateOf(mutableMapOf<Int, Long>())
    }
    var pickedPokemon by remember { mutableStateOf(-1L) }
    var pokemonsInTeam: List<Long> = listOf()
    var pokemonIdInTeam by remember { mutableStateOf(-1L) }

    if (edit) {
        pokemonsInTeam = getPokemonsFromTeamId(teamId)
    }

    if (!show) return
    AlertDialog(onDismissRequest = { close() }, title = {
        if (edit) {
            Text("Team edition")
        } else {
            Text("Team creation")
        }
    }, backgroundColor = Purple500, text = {
        Column(
            Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        ) {
            for (i in 1..6) {
                if (edit) {
                    pokemonIdInTeam = pokemonsInTeam[i - 1]
                }
                PickPokemon(pokemons, profile, edit, pokemonIdInTeam, getPokemonId = {
                    pickedPokemon = it
                })
                if (pickedPokemon != -1L) {
                    team.put(i, pickedPokemon)
                    pickedPokemon = -1L
                } else {
                    team.put(i, -1L)
                }
            }
        }
    }, confirmButton = {
        Row(
            modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center
        ) {
            Button(enabled = (team.size == 6), onClick = { close(); createTeam = true }) {
                Text("Done")
            }
        }
    })
    if (createTeam) {
        if (edit) {
            EditTeam(team.values.toList(), teamId)
        } else {
            AddTeamToDatabase(team.values.toList(), profile)
        }
    }

}

@Composable
fun PickPokemon(
    pokemons: Map<Long, Pokemon>,
    profile: Profile,
    edit: Boolean,
    pokemonIdInTeam: Long,
    getPokemonId: (Long) -> Unit
) {
    var showPokemonList by remember { mutableStateOf(false) }
    var currentPokemon by remember { mutableStateOf(-1L) }
    val copyPokemons by remember { mutableStateOf(pokemons) }

    if (edit) {
        currentPokemon = pokemonIdInTeam
    }

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
            PokemonListTeamDisplay(it) {}
            getPokemonId(it.id)
        }
        Button(
            onClick = { currentPokemon = -1L; getPokemonId(-1L) },
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
        PokedexDisplay(pokemons, profile, getPokemonId) {
            showPokemonList = false
        }
    }
}

@SuppressLint("MutableCollectionMutableState")
@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun PokedexDisplay(
    pokemons: Map<Long, Pokemon>,
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
                pokemonList = resultList,
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
                Row {
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
/*
@Preview
@Composable
fun PreviewPokemonTeamCard() {
    var clicked by remember { mutableStateOf(false) }
    var clicked2 by remember { mutableStateOf(false) }
    val backgroundColor = if (clicked) Color.Green else Color.Red
    val backgroundColor2 = if (clicked2) Color.Yellow else Color.Cyan
    Column(
        Modifier
            .padding(2.dp)
            .fillMaxHeight()
            .background(backgroundColor)
            .clickable { clicked = true }
    ) {
        Column(
            Modifier
                .padding(2.dp)
                .fillMaxHeight()
                .background(backgroundColor2)
                .clickable { clicked2 = true }
        ) {
            Text("a")
            Text("bbbbbbb")
            Text(text = "ccccc")
        }
    }
}
*/
@Preview
@Composable
fun PreviePokemonTeamCard() {
    var b by remember {
        mutableStateOf(false)
    }
    Box(
        modifier = Modifier
            .padding(4.dp)
            .height(70.dp)
            .fillMaxWidth()
            .background(Purple200, RoundedCornerShape(8.dp))
            .clickable {
                b = true
            },
    ) {
        Text("Choose Pokemon...", fontSize = 20.sp)

        if (b) {
            PreviewPokemonTeamCard()
        }
        Button(
            onClick = { },
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

@Composable
fun PreviewPokemonTeamCard() {
    var back by remember {
        mutableStateOf(Color.Red)
    }
    Row(
        Modifier
            .clickable(onClick = { back = Color.Green })
            .background(back)
            .height(70.dp)
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text("aaaaaaaa")
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .padding(horizontal = 8.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Bottom
            ) {
                Text("c")
                Spacer(modifier = Modifier.width(3.dp))
            }
            Text("bb")
        }
        Spacer(modifier = Modifier.weight(1.0f))
    }
}
