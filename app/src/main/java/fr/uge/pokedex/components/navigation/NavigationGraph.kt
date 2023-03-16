package fr.uge.pokedex.components.navigation


import android.annotation.SuppressLint
import android.content.Context
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import fr.uge.pokedex.components.pokedex.PokedexDisplay
import fr.uge.pokedex.components.team.DisplayTeams
import fr.uge.pokedex.components.search.FilterBar
import fr.uge.pokedex.components.card.PokemonCardDisplay
import fr.uge.pokedex.components.profile.ProfilesScreen
import fr.uge.pokedex.data.pokedex.Pokemon
import fr.uge.pokedex.data.user.Favorite
import fr.uge.pokedex.data.user.PokedexAppDatabaseConnection
import fr.uge.pokedex.data.user.Profile


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
    setCurrentProfile: (profile: Profile) -> Unit,
    profile: Profile,
    pokemonMap: Map<Long, Pokemon>
) {
    NavHost(navController = navController, startDestination = Route.Profiles.path) {

        composable(route = Route.Pokedex.path) {
            //Call pokedex composable

            var filteredPokemons by remember { mutableStateOf(mutableListOf<Pokemon>()) }

            Column() {
                FilterBar(pokemonList = pokemonMap.values.toList(), applicationContext = applicationContext)
                {
                    filteredPokemons = it.toMutableList()
                }

                PokedexDisplay(pokemonList = filteredPokemons,
                    profile = profile,
                    clickFavorite = { pokemonId, favorite ->
                        if(favorite != null)
                            PokedexAppDatabaseConnection.connection.favoriteDao().deleteFavorite(favorite)
                        else
                            PokedexAppDatabaseConnection.connection.favoriteDao().addFavorite(Favorite(pokemonId, profile.getId()))
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

            PokemonCardDisplay(
                pokemon,
                favoriteList = PokedexAppDatabaseConnection.connection.profileDao().getProfileWithFavorites(profile.getId()).favorites,
                onClickFavorite = { pokemonId, favorite ->
                    if(favorite != null)
                        PokedexAppDatabaseConnection.connection.favoriteDao().deleteFavorite(favorite)
                    else
                        PokedexAppDatabaseConnection.connection.favoriteDao().addFavorite(Favorite(pokemonId, profile.getId()))

                }
            )
        }
        composable(route = Route.Favorite.path) {
            //Call favorite composable
            val favorites = PokedexAppDatabaseConnection.connection.profileDao().getProfileWithFavorites(profile.getId()).favorites

            val favoritesPokemon = favorites.map { favorite -> pokemonMap.get(favorite.getPokemonId())!! }.toList()

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
                    clickFavorite = { pokemonId, favorite ->
                        if(favorite != null)
                            PokedexAppDatabaseConnection.connection.favoriteDao().deleteFavorite(favorite)
                        else
                            PokedexAppDatabaseConnection.connection.favoriteDao().addFavorite(Favorite(pokemonId, profile.getId()))
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
            ProfilesScreen(navController, setCurrentProfile)
        }
    }
}

