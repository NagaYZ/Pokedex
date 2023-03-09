package fr.uge.pokedex.components

import android.annotation.SuppressLint
import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
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
fun DisplayTeams(
    pokemons: Map<Long, Pokemon>, context: Context, favorites: List<Favorite>, profile: Profile
) {
    var showNewTeamDialog by remember { mutableStateOf(false) }
    var test = mutableListOf<Pokemon>()

    pokemons.get(1)?.let { test.add(it) }
    pokemons.get(2)?.let { test.add(it) }
    pokemons.get(3)?.let { test.add(it) }
    pokemons.get(4)?.let { test.add(it) }
    pokemons.get(5)?.let { test.add(it) }
    pokemons.get(6)?.let { test.add(it) }

    LazyVerticalGrid(
        columns = GridCells.Fixed(1),
        horizontalArrangement = Arrangement.spacedBy(40.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp),
        contentPadding = PaddingValues(30.dp, 35.dp)
    ) {
        items(5) {
            TeamDisplay(pokemon_team = test.toList(), context = context) {}
        }
    }
/*
    //display list of team
    //Add new team button
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

    if (showNewTeamDialog) {
        PopupWindow(
            show = showNewTeamDialog,
            pokemons,
            context,
            favorites,
            profile
        ) { showNewTeamDialog = false }
    }*/
}


@SuppressLint("MutableCollectionMutableState")
@Composable
fun PopupWindow(
    show: Boolean,
    pokemons: Map<Long, Pokemon>,
    context: Context,
    favorites: List<Favorite>,
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

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun PickPokemon(
    pokemons: Map<Long, Pokemon>,
    context: Context,
    favorites: List<Favorite>,
    profile: Profile,
    getPokemonId: (Long) -> Unit
) {
    var showPokemonList by remember { mutableStateOf(false) }

    var currentPokemon by remember {
        mutableStateOf(-1L)
    }
    var currentIconeFavori by remember {
        mutableStateOf(-1L)
    }
    val copyPokemons by remember {
        mutableStateOf(pokemons)
    }
    var resultList by remember {
        mutableStateOf(mutableListOf<Pokemon>())
    }
    var fav by remember {
        mutableStateOf(Favorite(-1L, -1L))
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
            PokemonTeamDisplay(it, context) {}
            getPokemonId(it.id)
        }
    }

    //show pokedex to select pokemon
    if (showPokemonList) {

        Dialog(
            onDismissRequest = { showPokemonList = false }, properties = DialogProperties(
                dismissOnBackPress = true, usePlatformDefaultWidth = false
            )
        ) {
            Column(
                modifier = Modifier
                    .background(MaterialTheme.colors.background)
                    .fillMaxSize()
            ) {
                FiltersBar(pokemons = pokemons.values.toList()) {
                    resultList = it.toMutableList()
                }

                DisplayPokedex(context = LocalContext.current,
                    pokemons = resultList,
                    favorites = favorites,
                    profile = profile,
                    getPokemonId = {
                        currentPokemon = it
                    },
                    getPokemonFavoriteId = {
                        currentIconeFavori = it
                    },
                    clickFavorite = {
                        fav = Favorite(currentIconeFavori, profile.getId())
                        if (!favorites.contains(fav)) {
                            PokedexAppDatabaseConnection.connection.favoriteDao().addFavorite(fav)
                            copyPokemons.get(currentIconeFavori)!!.isFavorite = true
                        }
                    },
                    onClick = { showPokemonList = false })
            }
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
                Box(
                    modifier = Modifier
                        .padding(4.dp)
                        .height(70.dp)
                        .fillMaxWidth()
                        .background(Color.Gray, RoundedCornerShape(8.dp))
                )
            }
        }

    }, confirmButton = {
        Button(onClick = { }) {
            Text("Done")
        }
    })
}



