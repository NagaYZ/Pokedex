package fr.uge.pokedex.components

import android.text.TextUtils.indexOf
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import fr.uge.pokedex.data.Pokemon
import fr.uge.pokedex.data.PokemonRepository
import fr.uge.pokedex.data.Type

sealed class Route(val title: String, val path: String){
    object Pokedex : Route("Pokedex","pokedex")
    object Favorite : Route("Favorite","favorite")
    object Teams : Route("Teams","teams")
    object Profiles : Route("Profiles","profiles")

    object Card : Route("Card", "card")
}

@Composable
fun NavigationGraph(navController: NavHostController){
    var currentPokemon by remember {
        mutableStateOf(Pokemon(-1L, "", Pair(Type.NONE, Type.NONE), 0,0,"","", "", true))
    }
    NavHost(navController = navController, startDestination =  Route.Profiles.path){
        composable(route = Route.Pokedex.path){
            //Call pokedex composable
            Column() {
                DisplayPokedex(context = LocalContext.current, pokemons = SearchBar(PokemonRepository(LocalContext.current).getAll().toList()), navController){
                    currentPokemon = it
                }
            }
        }
        composable(route = Route.Card.path){
            //Call a card pokemon composable
            PokemonCard(context = LocalContext.current, pokemon = PokemonRepository(LocalContext.current).getAll().toList().get((
                    currentPokemon!!.id.toInt())-1))
        }
        composable(route = Route.Favorite.path){
            //Call favorite composable
            Text(text = "Favorite screen", style = MaterialTheme.typography.h1)
        }
        composable(route = Route.Teams.path){
            //Call teams composable
            Text(text = "Teams screen", style = MaterialTheme.typography.h1)
        }
        composable(route = Route.Profiles.path){
            //Call teams composable
//            Text(text = "Profiles screen", style = MaterialTheme.typography.h1)
            ProfilesScreen(navController)
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