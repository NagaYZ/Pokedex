package fr.uge.pokedex


import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.getValue
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import fr.uge.pokedex.components.BottomNavigationMenu
import fr.uge.pokedex.components.NavigationGraph
import fr.uge.pokedex.components.Route
import fr.uge.pokedex.components.TopBar
import fr.uge.pokedex.data.PokemonRepository
import fr.uge.pokedex.database.Favorite
import fr.uge.pokedex.database.PokedexAppDatabaseConnection
import fr.uge.pokedex.database.Profile
import fr.uge.pokedex.ui.theme.PokedexTheme

class MainActivity : ComponentActivity() {
    private lateinit var pokemonRepository : PokemonRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        PokedexAppDatabaseConnection.initialise(applicationContext)
        pokemonRepository = PokemonRepository(applicationContext)

        setContent {
            PokedexTheme {
                val navController: NavHostController = rememberNavController()

//                PokedexAppDatabaseConnection.connection.profileDao().deleteAllProfiles()
//                PokedexAppDatabaseConnection.connection.favoriteDao().deleteAllFavorites()
//
//                val profileMatId:Long = PokedexAppDatabaseConnection.connection.profileDao().addProfile(Profile("Mat"))
//
//                PokedexAppDatabaseConnection.connection.favoriteDao().addFavorite(Favorite(2, profileMatId))
//
//                Log.d("FavoritesInProfile", PokedexAppDatabaseConnection.connection.profileDao().getProfileWithFavorites(profileMatId).toString())
//                Log.d("Favorites", PokedexAppDatabaseConnection.connection.favoriteDao().getFavorites().toString())


                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {

                    val currentBackStackEntry by navController.currentBackStackEntryAsState()
                    val currentRoute = currentBackStackEntry?.destination?.route

                    var currentProfile by remember { mutableStateOf(Profile("")) }

                    Scaffold(bottomBar = { if(currentRoute != Route.Profiles.path) BottomNavigationMenu(navController)},
                        topBar = { if(currentRoute != Route.Profiles.path) TopBar(navController, currentProfile)  }) {
                        Log.d("Padding",it.toString())
                        NavigationGraph(navController = navController, setCurrentProfile = {profile: Profile -> currentProfile = profile })
                    }
                }

            }
        }
    }
}