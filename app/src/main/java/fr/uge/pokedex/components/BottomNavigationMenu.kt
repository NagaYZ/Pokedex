package fr.uge.pokedex.components


import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import fr.uge.pokedex.data.Pokemon
import fr.uge.pokedex.database.Favorite
import fr.uge.pokedex.database.PokedexAppDatabaseConnection
import fr.uge.pokedex.database.Profile


sealed class Route(val title: String, val path: String){
    object Pokedex : Route("Pokedex","pokedex")
    object Favorite : Route("Favorite","favorite")
    object Teams : Route("Teams","teams")
    object Profiles : Route("Profiles","profiles")

    object Card : Route("Card", "card")
}

@Composable
fun NavigationGraph(navController: NavHostController, setCurrentProfile :(profile : Profile) -> Unit, profile: Profile, pokemonMap: Map<Long, Pokemon>){
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
        mutableStateOf(Favorite(-1L, -1L))
    }
    var resultList by remember {
        mutableStateOf(mutableListOf<Pokemon>())
    }

    NavHost(navController = navController, startDestination =  Route.Profiles.path){

        composable(route = Route.Pokedex.path) {
            //Call pokedex composable
            var favorites by remember {
                mutableStateOf(PokedexAppDatabaseConnection.connection.profileDao().getProfileWithFavorites(profile.getId()).favorites.distinct())
            }
            Column() {
                FiltersBar(pokemonList = copyPokemonMap.values.toList())
                {
                    resultList = it.toMutableList()
                }

                DisplayPokedex(context = LocalContext.current,
                    pokemonList = resultList,
                    navController = navController,
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
                                println("deleted 1")
                                favorites.forEach { favorite ->
                                    println("deleted 2")
                                    if (favorite.getPokemonId() == currentIconFavorite) {
                                        println("deleted")
                                        PokedexAppDatabaseConnection.connection.favoriteDao()
                                            .deleteFavorite(favorite)
                                        favorites = PokedexAppDatabaseConnection.connection.profileDao().getProfileWithFavorites(profile.getId()).favorites
                                    }
                                }
                            } else {
                                fav = Favorite(currentIconFavorite, profile.getId())
                                if (!favorites.contains(fav)) {
                                    PokedexAppDatabaseConnection.connection.favoriteDao().addFavorite(fav)
                                    favorites = PokedexAppDatabaseConnection.connection.profileDao().getProfileWithFavorites(profile.getId()).favorites
                            }
                        }
                    })
            }
        }


        composable(route = Route.Card.path){
            //Call a card pokemon composable
            var pokemon by remember {
                mutableStateOf(copyPokemonMap.get(currentPokemon)!!)
            }
            val favorites by remember {
                mutableStateOf(PokedexAppDatabaseConnection.connection.profileDao().getProfileWithFavorites(profile.getId()).favorites.distinct())
            }
            PokemonBoxDisplay(context = LocalContext.current, pokemon = pokemon, onClickFavorite = {
                if (!it) {
                    favorites.forEach { favorite ->
                        if (favorite.getPokemonId() == pokemon.id) {
                            PokedexAppDatabaseConnection.connection.favoriteDao()
                                .deleteFavorite(favorite)
                        }
                    }
                } else {
                    fav = Favorite(pokemon.id, profile.getId())
                    if (!favorites.contains(fav)) {

                        PokedexAppDatabaseConnection.connection.favoriteDao().addFavorite(fav)
                    }
                }

            },
                favoriteList = PokedexAppDatabaseConnection.connection.profileDao().getProfileWithFavorites(profile.getId()).favorites.map { it.getPokemonId() }.toList()
            )
        }
        composable(route = Route.Favorite.path){
            //Call favorite composable
            val favorites by remember {
                mutableStateOf(PokedexAppDatabaseConnection.connection.profileDao().getProfileWithFavorites(profile.getId()).favorites.distinct())
            }

            var pokemonsFav by remember {
                mutableStateOf(mutableListOf<Pokemon>())
            }

            favorites.forEach { favorite ->
                pokemonsFav.add(copyPokemonMap.get(favorite.getPokemonId())!!)}

            Column() {
                FiltersBar(pokemonList = pokemonsFav.distinct(), filterList = {
                    resultList = it.toMutableList()
                })

                DisplayPokedex(
                    sizeGrid = 1,
                    context = LocalContext.current,
                    pokemonList = resultList,
                    navController = navController,
                    profile = profile,
                    getPokemonId = {
                        currentPokemon = it
                    }, getPokemonFavoriteId = { currentIconFavorite = it },
                    clickFavorite = {
                        favorites.forEach { favorite ->
                            if (favorite.getPokemonId() == currentIconFavorite) {
                                PokedexAppDatabaseConnection.connection.favoriteDao().deleteFavorite(favorite)
                            }
                        }
                    })

            }
        }
        composable(route = Route.Teams.path){
            //Call teams composable
            Text(text = "Teams screen", style = MaterialTheme.typography.h1)
        }
        composable(route = Route.Profiles.path){
            //Call teams composable
//            Text(text = "Profiles screen", style = MaterialTheme.typography.h1)
            ProfilesScreen(navController, setCurrentProfile)
        }
    }
}

@Composable
fun BottomNavigationMenu(navController: NavHostController){
    Row(modifier = Modifier
        .fillMaxWidth()
        .height(40.dp)
        .background(color = MaterialTheme.colors.primary), horizontalArrangement = Arrangement.SpaceEvenly, verticalAlignment = Alignment.CenterVertically) {

        BottomMenuButton(Route.Pokedex, navController)
        BottomMenuButton(Route.Favorite, navController)
        BottomMenuButton(Route.Teams, navController)
    }
}

@Composable
fun BottomMenuButton(route: Route, navController: NavHostController){

    val currentBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = currentBackStackEntry?.destination?.route

    Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.SpaceEvenly, modifier = Modifier.clickable {
        navController.navigate(route.path)
    }) {
        if(currentRoute == route.path) Text(text = route.title, style = MaterialTheme.typography.button, modifier = Modifier.padding(12.dp), color = MaterialTheme.colors.onPrimary)
        else Text(text = route.title, style = MaterialTheme.typography.button, modifier = Modifier.padding(12.dp))
    }
}