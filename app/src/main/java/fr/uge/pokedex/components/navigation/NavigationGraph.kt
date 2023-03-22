package fr.uge.pokedex.components.navigation


import android.annotation.SuppressLint
import android.content.Context
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import fr.uge.pokedex.components.card.PokemonCardDisplay
import fr.uge.pokedex.components.pokedex.PokedexDisplay
import fr.uge.pokedex.components.profile.ProfilesScreen
import fr.uge.pokedex.components.search.FilterBar
import fr.uge.pokedex.components.team.DisplayTeams
import fr.uge.pokedex.data.pokedex.PokedexStorageService
import fr.uge.pokedex.data.pokedex.pokemon.Pokemon
import fr.uge.pokedex.data.user.Favorite
import fr.uge.pokedex.data.user.PokedexAppDatabase
import fr.uge.pokedex.service.PokemonMusicService
import kotlinx.coroutines.runBlocking

sealed class Route(val title: String, val path: String) {
    object Pokedex : Route("Pokedex", "pokedex")
    object Favorite : Route("Favorite", "favorite")
    object Teams : Route("Teams", "teams")
    object Profiles : Route("Profiles", "profiles")

    object Card : Route("Card", "card/{pokemonId}")
}

@SuppressLint("MutableCollectionMutableState")
@Composable
fun NavigationGraph(
    applicationContext: Context,
    navController: NavHostController,
    setCurrentProfile: (profileId: Long) -> Unit,
    profileId: Long,
    pokemonMusicService: PokemonMusicService,
    pokemonMap: Map<Long, Pokemon> = PokedexStorageService.getPokemonData()
) {
    val context = LocalContext.current
    val profile = runBlocking { PokedexAppDatabase.getConnection(context).profileDao().getProfile(profileId) }
    NavHost(navController = navController, startDestination = Route.Profiles.path) {

        composable(route = Route.Pokedex.path) {
            //Call pokedex composable
            var favoriteList by remember{ mutableStateOf(runBlocking {  PokedexAppDatabase.getConnection(context).profileDao().getProfileWithFavorites(profileId).favorites }) }
            var filteredPokemon by remember { mutableStateOf(mutableListOf<Pokemon>()) }

            Column() {
                FilterBar(pokemonList = pokemonMap.values.toList(), applicationContext = applicationContext)
                {
                    filteredPokemon = it.toMutableList()
                }

                PokedexDisplay(pokemonList = filteredPokemon,
                    profile = profile,
                    favoriteList = favoriteList,
                    clickFavorite = { pokemonId, favorite ->
                        if(favorite != null){
                            runBlocking { PokedexAppDatabase.getConnection(context).favoriteDao().deleteFavorite(favorite) }
                        }
                        else{
                            runBlocking {
                                PokedexAppDatabase.getConnection(context).favoriteDao().addFavorite(Favorite(pokemonId, profile.getId()))
                                favoriteList = runBlocking {  PokedexAppDatabase.getConnection(context).profileDao().getProfileWithFavorites(profileId).favorites }
                            }
                        }
                    },
                    onClick = { pokemonId ->
                        navController.navigate("card/$pokemonId")
                    })
            }
        }

        composable(route = Route.Card.path, arguments = listOf(
            navArgument("pokemonId"){
                type = NavType.LongType
            }
        )) {
            //Call a card pokemon composable
            val pokemon by remember {
                mutableStateOf(pokemonMap[it.arguments?.getLong("pokemonId")]!!)
            }

            val favoriteList = runBlocking {  PokedexAppDatabase.getConnection(context).profileDao().getProfileWithFavorites(profileId).favorites }

            PokemonCardDisplay(
                pokemon,
                favoriteList = favoriteList,
                onClickFavorite = { pokemonId, favorite ->
                    if(favorite != null)
                        runBlocking {PokedexAppDatabase.getConnection(context).favoriteDao().deleteFavorite(favorite)}
                    else
                        runBlocking {PokedexAppDatabase.getConnection(context).favoriteDao().addFavorite(Favorite(pokemonId, profile.getId()))}

                }
            )
        }
        composable(route = Route.Favorite.path) {
            //Call favorite composable
            var favoriteList by remember{ mutableStateOf(runBlocking {  PokedexAppDatabase.getConnection(context).profileDao().getProfileWithFavorites(profileId).favorites }) }
//            val favorites = favoriteList

            val favoritesPokemon = favoriteList.map { favorite -> pokemonMap.get(favorite.getPokemonId())!! }.toList()

            var filteredPokemons by remember {
                mutableStateOf(mutableListOf<Pokemon>())
            }

            Column() {
                FilterBar(pokemonList = favoritesPokemon.distinct(), filterList = {
                    filteredPokemons = it.toMutableList()
                }, applicationContext = applicationContext)

                PokedexDisplay(
                    sizeGrid = 1,
                    pokemonList = filteredPokemons,
                    profile = profile,
                    favoriteList = favoriteList,
                    clickFavorite = { pokemonId, favorite ->
                        if(favorite != null){
                            runBlocking {PokedexAppDatabase.getConnection(context).favoriteDao().deleteFavorite(favorite)}
                        }
                        else{
                            runBlocking {
                                PokedexAppDatabase.getConnection(context).favoriteDao().addFavorite(Favorite(pokemonId, profile.getId()))
                                favoriteList = runBlocking {  PokedexAppDatabase.getConnection(context).profileDao().getProfileWithFavorites(profileId).favorites }
                            }
                        }
                    },
                    onClick = { pokemonId ->
                        navController.navigate("card/$pokemonId")
                    })

            }
        }
        composable(route = Route.Teams.path) {
            //Call teams composable
            DisplayTeams(
                pokemonMap,
                profile,
                onPokemonClick = { pokemonId ->
                    navController.navigate("card/$pokemonId")
                }
            )
        }

        composable(route = Route.Profiles.path){
            //Call profiles composable
            ProfilesScreen(navController, setCurrentProfile, pokemonMusicService)
        }
    }
}

