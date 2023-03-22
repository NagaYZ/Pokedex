package fr.uge.pokedex.components.team

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import fr.uge.pokedex.components.pokedex.PokedexDisplay
import fr.uge.pokedex.components.search.FilterBar
import fr.uge.pokedex.data.pokedex.pokemon.Pokemon
import fr.uge.pokedex.data.user.Favorite
import fr.uge.pokedex.data.user.PokedexAppDatabase
import fr.uge.pokedex.data.user.Profile
import fr.uge.pokedex.theme.Purple200
import kotlinx.coroutines.runBlocking

@Composable
fun PokemonSelection(
    pokemonMap: Map<Long, Pokemon>,
    profile: Profile,
    edit: Boolean,
    pokemonIdInTeam: Long,
    getPokemonId: (Long) -> Unit
) {
    var showPokemonList by remember { mutableStateOf(false) }
    var currentPokemon by remember { mutableStateOf(-1L) }

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
        pokemonMap[currentPokemon]?.let {
            DisplaySelectedPokemon(it) { showPokemonList = true }
            getPokemonId(it.id)
        }
    }

    if (showPokemonList) {
        PokemonSelectionDialog(pokemonList = pokemonMap.values.toList(), profile = profile, dismiss = { showPokemonList = false}, onClick = { pokemonId ->
            currentPokemon = pokemonId
            showPokemonList = false
        })
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun PokemonSelectionDialog(pokemonList:List<Pokemon>, profile:Profile, dismiss:() -> Unit, onClick:(Long) -> Unit) {
    val context = LocalContext.current
    val run = runBlocking {
        PokedexAppDatabase.getConnection(context).profileDao()
            .getProfileWithFavorites(profile.getId()).favorites
    }
    var favoriteList by remember { mutableStateOf(run) }

    Dialog(
        onDismissRequest = { dismiss.invoke() },
        properties = DialogProperties(dismissOnBackPress = true, usePlatformDefaultWidth = false)
    ) {
        Column(
            Modifier
                .background(MaterialTheme.colors.background)
                .fillMaxSize()
        ) {

            var filteredPokemons by remember {
                mutableStateOf(mutableListOf<Pokemon>())
            }
            FilterBar(pokemonList = pokemonList, filterList = {
                filteredPokemons = it.toMutableList()
            }, applicationContext = context)

            PokedexDisplay(pokemonList = filteredPokemons,
                favoriteList = favoriteList,
                clickFavorite = { pokemonId, favorite ->
                    if (favorite != null) {
                        runBlocking {
                            PokedexAppDatabase.getConnection(context).favoriteDao()
                                .deleteFavorite(favorite)
                        }
                    } else {
                        runBlocking {
                            PokedexAppDatabase.getConnection(context).favoriteDao().addFavorite(
                                Favorite(pokemonId, profile.getId())
                            )
                            favoriteList = runBlocking { run }
                        }
                    }
                },
                onClick = { pokemonId -> onClick.invoke(pokemonId) })
        }
    }
}