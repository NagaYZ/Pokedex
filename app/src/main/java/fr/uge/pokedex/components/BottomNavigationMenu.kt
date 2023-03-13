package fr.uge.pokedex.components


import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import fr.uge.pokedex.data.Pokemon
import fr.uge.pokedex.database.Favorite
import fr.uge.pokedex.database.PokedexAppDatabaseConnection
import fr.uge.pokedex.database.Profile


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
    val copyPokemonMap by remember {
        mutableStateOf(pokemonMap)
    }

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
                FiltersBar(pokemonList = copyPokemonMap.values.toList())
                {
                    resultList = it.toMutableList()
                }

                DisplayPokedex(pokemonList = resultList,
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
            var pokemon by remember {
                mutableStateOf(copyPokemonMap.get(currentPokemon)!!)
            }
            var favorites by remember {
                mutableStateOf(
                    PokedexAppDatabaseConnection.connection.profileDao()
                        .getProfileWithFavorites(profile.getId()).favorites.distinct()
                )
            }
            PokemonInfoDisplay(
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
            var favorites by remember {
                mutableStateOf(
                    PokedexAppDatabaseConnection.connection.profileDao()
                        .getProfileWithFavorites(profile.getId()).favorites
                )
            }

            var favoritesPokemon by remember {
                mutableStateOf(mutableListOf<Pokemon>())
            }

            favorites.forEach { favorite ->
                favoritesPokemon.add(copyPokemonMap.get(favorite.getPokemonId())!!)
            }

            Column() {
                FiltersBar(pokemonList = favoritesPokemon.distinct(), filterList = {
                    resultList = it.toMutableList()
                })

                DisplayPokedex(
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

@Composable
fun BottomNavigationMenu(navController: NavHostController) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = MaterialTheme.colors.primary)
            .padding(7.dp),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {

        BottomMenuButton(Route.Pokedex, navController, Icons.Filled.Menu)
        BottomMenuButton(Route.Favorite, navController, Icons.Filled.Favorite)
        BottomMenuButton(Route.Teams, navController, Icons.Filled.Add)
    }
}

@Composable
fun BottomMenuButton(
    route: Route,
    navController: NavHostController,
    icon: ImageVector
) {

    val currentBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = currentBackStackEntry?.destination?.route

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceEvenly,
        modifier = Modifier.clickable {
            navController.navigate(route.path)
        }
    ) {
        if (currentRoute == route.path) {
            Icon(
                imageVector = icon,
                contentDescription = "Favorite",
                modifier = Modifier
                    .padding(5.dp)
                    .size(ButtonDefaults.IconSize)
                    .scale(1.3f),
                tint = Color.White
            )
            Text(
                text = route.title,
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.subtitle2,
                color = MaterialTheme.colors.onPrimary
            )
        } else {
            Icon(
                imageVector = icon,
                contentDescription = "Favorite",
                modifier = Modifier
                    .padding(5.dp)
                    .size(ButtonDefaults.IconSize)
                    .scale(1.2f),
                tint = Color(0x65FFFFFF)
            )
            Text(
                text = route.title,
                style = MaterialTheme.typography.subtitle2,
                color = Color(0x99FFFFFF)
            )
        }
    }
}