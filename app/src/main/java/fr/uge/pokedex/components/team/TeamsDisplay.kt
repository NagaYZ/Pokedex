package fr.uge.pokedex.components.team

import android.annotation.SuppressLint
import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import fr.uge.pokedex.data.pokedex.pokemon.Pokemon
import fr.uge.pokedex.data.team.TeamFactGenerator
import fr.uge.pokedex.data.user.*
import fr.uge.pokedex.theme.Purple500
import kotlinx.coroutines.runBlocking

private fun DeleteTeam(teamId: Long, context: Context) {
    val teamDao: TeamDao = PokedexAppDatabase.getConnection(context).teamDao()
    val team: Team = runBlocking {teamDao.getTeam(teamId)}
    runBlocking {teamDao.deleteTeam(team)}
}

private fun getTeamsFromProfile(profile: Profile, context: Context): List<TeamWithMembers> {
    val profileDao: ProfileDao = PokedexAppDatabase.getConnection(context).profileDao()
    return runBlocking {profileDao.getProfileWithTeam(profile.getId()).teamsWithMembers}
}

private fun getPokemonListFromTeamId(teamId: Long, context: Context): List<Long> {
    val teamDao: TeamDao = PokedexAppDatabase.getConnection(context).teamDao()
    val teamWithMembers = runBlocking {teamDao.getTeamWithMembers(teamId)}
    return teamWithMembers.teamMembers.map { member -> member.getPokemonId() }
}

private fun editTeam(pokemonList: List<Long>, teamId: Long, context: Context) {
    val teamDao: TeamDao = PokedexAppDatabase.getConnection(context).teamDao()
    val teamMemberDao: TeamMemberDao = PokedexAppDatabase.getConnection(context).teamMemberDao()
    val teamWithMembers = runBlocking {teamDao.getTeamWithMembers(teamId)}

    for (i in 0..5) {
        runBlocking {teamMemberDao.deleteTeamMember(teamWithMembers.teamMembers[i])}
    }

    pokemonList.forEach { pokemon ->
        runBlocking {teamMemberDao.addTeamMember(TeamMember(pokemon, teamId))}
    }
}

private fun addTeamToDatabase(team: List<Long>, profile: Profile, context: Context) {
    val teamDao: TeamDao = PokedexAppDatabase.getConnection(context).teamDao()
    val teamMemberDao: TeamMemberDao = PokedexAppDatabase.getConnection(context).teamMemberDao()
    val teamId: Long = runBlocking {teamDao.addTeam(Team("Team de " + profile.getProfileName(), profile.getId()))}

    team.forEach { pokemon ->
        runBlocking {teamMemberDao.addTeamMember(TeamMember(pokemon, teamId))}
    }
}

@Composable
fun DisplayTeams(
    pokemonMap: Map<Long, Pokemon>,
    profile: Profile,
    onPokemonClick: (Long) -> Unit
) {
    val context = LocalContext.current
    var showNewTeamDialog by remember { mutableStateOf(false) }
    var showTeamCard by remember { mutableStateOf(false) }
    var delete by remember { mutableStateOf(false) }
    var edit by remember { mutableStateOf(false) }
    var teamId by remember { mutableStateOf(-1L) }
    val teams = getTeamsFromProfile(profile = profile, context)

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
                pokemonMap,
                { teamId = it; edit = true; showNewTeamDialog = true },
                { teamId = it; delete = true },
                { teamId = it; showTeamCard = true },
                onPokemonClick
            )
        }
        item {
            Spacer(modifier = Modifier.padding(bottom = 40.dp))
        }
    }

    //Button : Add New Team
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Bottom,
        horizontalAlignment = Alignment.End
    ) {
        Button(modifier = Modifier.padding(vertical = 70.dp, horizontal = 20.dp), onClick = {
            showNewTeamDialog = true
        }) {
            Icon(Icons.Rounded.Add, "Add Team")
        }
    }

    if (delete) {
        DeleteTeam(teamId, context)
        delete = false
        Toast.makeText(context, "Team deleted successfully", Toast.LENGTH_SHORT).show()
    }

    if (showNewTeamDialog) {
        NewTeamDialog(
            pokemonMap,
            profile,
            teamId,
            edit,
        ) { showNewTeamDialog = false; edit = false }
    }

    if (showTeamCard) {
        ShowTeamCard(
            pokemonMap,
            getPokemonListFromTeamId(teamId = teamId, context),
            onPokemonClick
        ) { showTeamCard = false }

    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun ShowTeamCard(
    pokemonList: Map<Long, Pokemon>,
    pokemonTeam: List<Long>,
    onPokemonClick: (Long) -> Unit,
    onClick: () -> Unit
) {
    val teamFactGenerator = TeamFactGenerator()
    val pokemonInTeam: MutableList<Pokemon> = mutableListOf()

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
            Column {
                for (i in 0..1) {
                    Row(
                        Modifier
                            .height(IntrinsicSize.Max)
                            .padding(2.dp)
                            .fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        for (j in 0..2) {
                            val pokemon = pokemonList[pokemonTeam[i * 3 + j]]
                            Box(
                                Modifier
                                    .weight(1 / 3f)
                                    .padding(2.dp)
                            ) {
                                if (pokemon != null) {
                                    pokemonInTeam.add(i * 3 + j, pokemon)
                                    PokemonTeamCard(pokemon = pokemon, onPokemonClick)
                                }
                            }
                        }
                    }
                }
            }

            val facts = teamFactGenerator.getTeamFacts(pokemonInTeam.toList())
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colors.background),
                contentPadding = PaddingValues(10.dp)
            ) {
                item {
                    TeamFactListDisplay(facts)
                }
            }
        }
    }
}

@SuppressLint("MutableCollectionMutableState")
@Composable
fun NewTeamDialog(
    pokemonMap: Map<Long, Pokemon>,
    profile: Profile,
    teamId: Long,
    edit: Boolean,
    close: () -> Unit
) {
    val context = LocalContext.current
    var createTeam by remember { mutableStateOf(false) }
    val team by remember { mutableStateOf(mutableMapOf<Int, Long>()) }
    var pickedPokemon by remember { mutableStateOf(-1L) }
    var pokemonInTeam: List<Long> = listOf()
    var pokemonIdInTeam by remember { mutableStateOf(-1L) }
    var enableButton by remember { mutableStateOf(false) }
    var once by remember { mutableStateOf(false) }

    if (edit) {
        once = true
        pokemonInTeam = getPokemonListFromTeamId(teamId, context)
    }
    AlertDialog(
        onDismissRequest = { close() },
        title = { },
        backgroundColor = Purple500,
        text = {
            Column(
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 4.dp)
            ) {
                for (i in 1..6) {
                    if (edit && once) {
                        pokemonIdInTeam = pokemonInTeam[i - 1]
                    }
                    PokemonSelection(
                        pokemonMap,
                        profile,
                        edit && once,
                        pokemonIdInTeam,
                        getPokemonId = {
                            pickedPokemon = it
                        })
                    enableButton = team.filterValues { id -> id != -1L }.size == 6
                    if (pickedPokemon != -1L) {
                        team.put(i, pickedPokemon)
                        pickedPokemon = -1L
                    } else {
                        team.put(i, -1L)
                    }
                }
                once = false
            }
        }, confirmButton = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                Button(
                    enabled = enableButton,
                    onClick = {
                        createTeam = true
                    }) {
                    Text("Done")
                }
            }
        })
    if (createTeam) {
        if (edit) {
            editTeam(team.values.toList(), teamId, context)
            Toast.makeText(context, "Team edited successfully", Toast.LENGTH_SHORT).show()
        } else {
            addTeamToDatabase(team.values.toList(), profile, context)
            Toast.makeText(context, "Team created successfully", Toast.LENGTH_SHORT).show()
        }
        close()
    }
}
