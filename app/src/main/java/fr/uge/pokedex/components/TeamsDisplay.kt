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
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import fr.uge.pokedex.data.Pokemon
import fr.uge.pokedex.database.*
import fr.uge.pokedex.team.TeamFactGenerator
import fr.uge.pokedex.ui.theme.Purple200
import fr.uge.pokedex.ui.theme.Purple500

@Composable
private fun DeleteTeam(teamId: Long) {
    val teamDao: TeamDao = PokedexAppDatabaseConnection.connection.teamDao()
    val team: Team = teamDao.getTeam(teamId)
    teamDao.deleteTeam(team)
}

@Composable
private fun getTeamsFromProfile(profile: Profile): List<TeamWithMembers> {
    val profileDao: ProfileDao = PokedexAppDatabaseConnection.connection.profileDao()
    return profileDao.getProfileWithTeam(profile.getId()).teamsWithMembers
}

@Composable
private fun getPokemonsFromTeamId(teamId: Long): List<Long> {
    val teamDao: TeamDao = PokedexAppDatabaseConnection.connection.teamDao()
    val teamWithMembers = teamDao.getTeamWithMembers(teamId)
    return teamWithMembers.teamMembers.map { member -> member.getPokemonId() }
}

@Composable
fun EditTeam(pokemonList: List<Long>, teamId: Long) {
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
    onPokemonClick: (Long) -> Unit
) {
    var showNewTeamDialog by remember { mutableStateOf(false) }
    var showTeamCard by remember { mutableStateOf(false) }
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
                pokemons,
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
        DeleteTeam(teamId)
        delete = false
    }

    if (showNewTeamDialog) {
        NewTeamDialog(
            pokemons,
            profile,
            teamId,
            edit,
        ) { showNewTeamDialog = false; edit = false }
    }

    if (showTeamCard) {
        ShowTeamCard(
            pokemons,
            getPokemonsFromTeamId(teamId = teamId),
            onPokemonClick
        ) { showTeamCard = false }

    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun ShowTeamCard(
    pokemonList: Map<Long, Pokemon>,
    poketeam: List<Long>,
    onPokemonClick: (Long) -> Unit,
    onClick: () -> Unit
) {
    val teamFactGenerator = TeamFactGenerator()
    var pokemonsInTeam: MutableList<Pokemon> = mutableListOf()

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
            Column() {
                for (i in 0..1) {
                    Row(
                        Modifier
                            .height(IntrinsicSize.Max)
                            .padding(2.dp)
                            .fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        for (j in 0..2) {
                            val pokemon = pokemonList.get(poketeam[i * 3 + j])
                            Box(
                                Modifier
                                    .weight(1 / 3f)
                                    .padding(2.dp)
                            ) {
                                if (pokemon != null) {
                                    pokemonsInTeam.add(i * 3 + j, pokemon)
                                    PokemonTeamCard(pokemon = pokemon, onPokemonClick)
                                }
                            }
                        }
                    }
                }
            }

            val facts = teamFactGenerator.getTeamFacts(pokemonsInTeam.toList())
            Column(
                Modifier
                    .padding(15.dp)
                    .fillMaxSize()
                    .background(MaterialTheme.colors.background)
            ) {
                facts.forEach { fact ->
                    Text(fact.toString())
                }
            }
        }
    }
}

@SuppressLint("MutableCollectionMutableState")
@Composable
fun NewTeamDialog(
    pokemons: Map<Long, Pokemon>,
    profile: Profile,
    teamId: Long,
    edit: Boolean,
    close: () -> Unit
) {
    var createTeam by remember { mutableStateOf(false) }
    val team by remember { mutableStateOf(mutableMapOf<Int, Long>()) }
    var pickedPokemon by remember { mutableStateOf(-1L) }
    var pokemonsInTeam: List<Long> = listOf()
    var pokemonIdInTeam by remember { mutableStateOf(-1L) }
    var enableButton by remember { mutableStateOf(false) }
    var once by remember { mutableStateOf(false) }

    if (edit) {
        once = true
        pokemonsInTeam = getPokemonsFromTeamId(teamId)
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
                        pokemonIdInTeam = pokemonsInTeam[i - 1]
                    }
                    PokemonSelection(
                        pokemons,
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
            EditTeam(team.values.toList(), teamId)
        } else {
            AddTeamToDatabase(team.values.toList(), profile)
        }
        close()
    }
}

@Composable
fun PokemonSelection(
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
        contentAlignment = Alignment.Center
    ) {
        Text("Choose Pokemon...", fontSize = 20.sp, textAlign = TextAlign.Center)
        copyPokemons.get(currentPokemon)?.let {
            DisplaySelectedPokemon(it) { showPokemonList = true }
            getPokemonId(it.id)
        }
    }

    //show pokedex to select pokemon
    if (showPokemonList) {
        PokedexDisplay(pokemons, profile, getPokemonId = { currentPokemon = it }) {
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
                        favorites.forEach { favorite ->
                            if (favorite.getPokemonId() == currentIconFavorite) {
                                PokedexAppDatabaseConnection.connection.favoriteDao()
                                    .deleteFavorite(favorite)
                                favorites = PokedexAppDatabaseConnection.connection.profileDao()
                                    .getProfileWithFavorites(profile.getId()).favorites
                            }
                        }
                    } else {
                        fav = Favorite(currentIconFavorite, profile.getId())
                        if (!favorites.contains(fav)) {
                            PokedexAppDatabaseConnection.connection.favoriteDao()
                                .addFavorite(fav)
                            favorites = PokedexAppDatabaseConnection.connection.profileDao()
                                .getProfileWithFavorites(profile.getId()).favorites
                        }
                    }
                },
                onClick = { onClick() })
        }
    }
}
