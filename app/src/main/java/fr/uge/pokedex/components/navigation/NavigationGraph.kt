package fr.uge.pokedex.components.navigation


import android.annotation.SuppressLint
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
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

    object Card : Route("Card", "card")
}

@SuppressLint("MutableCollectionMutableState")
@Composable
fun NavigationGraph(
    navController: NavHostController,
    setCurrentProfile: (profile: Profile) -> Unit,
    profile: Profile,
    pokemonMap: Map<Long, Pokemon>
) {
    var currentPokemon by remember {
        mutableStateOf(-1L)
    }
    var currentIconFavorite by remember {
        mutableStateOf(-1L)
    }
    var fav by remember {
        mutableStateOf(Favorite(0L, 0L))
    }
    var resultList by remember {
        mutableStateOf(mutableListOf<Pokemon>())
    }

    NavHost(navController = navController, startDestination = Route.Profiles.path) {

        composable(route = Route.Pokedex.path) {
            //Call pokedex composable
            var favorites by remember {
                mutableStateOf(
                    PokedexAppDatabaseConnection.connection.profileDao()
                        .getProfileWithFavorites(profile.getId()).favorites
                )
            }
            Column() {
                FilterBar(pokemonList = pokemonMap.values.toList())
                {
                    resultList = it.toMutableList()
                }

                PokedexDisplay(pokemonList = resultList,
                    profile = profile,
                    getPokemonId = {
                        currentPokemon = it
                    },
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
                    onClick = {
                        navController.navigate("card")
                    })
            }
        }

        composable(route = Route.Card.path) {
            //Call a card pokemon composable
            val pokemon by remember {
                mutableStateOf(pokemonMap[currentPokemon]!!)
            }
            var favorites by remember {
                mutableStateOf(
                    PokedexAppDatabaseConnection.connection.profileDao()
                        .getProfileWithFavorites(profile.getId()).favorites.distinct()
                )
            }
            PokemonCardDisplay(
                pokemon,
                favoriteList = PokedexAppDatabaseConnection.connection.profileDao()
                    .getProfileWithFavorites(profile.getId()).favorites.map { it.getPokemonId() }
                    .toList(),
                onClickFavorite = {
                    if (!it) {
                        favorites.forEach { favorite ->
                            if (favorite.getPokemonId() == pokemon.id) {
                                PokedexAppDatabaseConnection.connection.favoriteDao()
                                    .deleteFavorite(favorite)
                                favorites = PokedexAppDatabaseConnection.connection.profileDao()
                                    .getProfileWithFavorites(profile.getId()).favorites

                            }
                        }
                    } else {
                        fav = Favorite(pokemon.id, profile.getId())
                        if (!favorites.contains(fav)) {
                            PokedexAppDatabaseConnection.connection.favoriteDao().addFavorite(fav)
                            favorites = PokedexAppDatabaseConnection.connection.profileDao()
                                .getProfileWithFavorites(profile.getId()).favorites

                        }
                    }

                }
            )
        }
        composable(route = Route.Favorite.path) {
            //Call favorite composable
            val favorites by remember {
                mutableStateOf(
                    PokedexAppDatabaseConnection.connection.profileDao()
                        .getProfileWithFavorites(profile.getId()).favorites
                )
            }

            val favoritesPokemon by remember {
                mutableStateOf(mutableListOf<Pokemon>())
            }

            favorites.forEach { favorite ->
                favoritesPokemon.add(pokemonMap[favorite.getPokemonId()]!!)
            }

            Column() {
                FilterBar(pokemonList = favoritesPokemon.distinct(), filterList = {
                    resultList = it.toMutableList()
                })

                PokedexDisplay(
                    sizeGrid = 1,
                    pokemonList = resultList,
                    profile = profile,
                    getPokemonId = {
                        currentPokemon = it
                    },
                    getPokemonFavoriteId = { currentIconFavorite = it },
                    clickFavorite = {
                        favorites.forEach { favorite ->
                            if (favorite.getPokemonId() == currentIconFavorite) {
                                PokedexAppDatabaseConnection.connection.favoriteDao()
                                    .deleteFavorite(favorite)
                            }
                        }
                    },
                    onClick = {
                        navController.navigate("card")
                    })

            }
        }
        composable(route = Route.Teams.path) {
            //Call teams composable
            DisplayTeams(
                pokemonMap,
                profile,
                onPokemonClick = {
                    currentPokemon = it
                    navController.navigate("card")
                }
            )
        }

        composable(route = Route.Profiles.path){
            //Call profiles composable
            ProfilesScreen(navController, setCurrentProfile)
        }
    }
}

