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
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.room.Room
import fr.uge.pokedex.components.BottomNavigationMenu
import fr.uge.pokedex.components.NavigationGraph
import fr.uge.pokedex.components.Route
import fr.uge.pokedex.components.TopBar
import fr.uge.pokedex.data.PokemonRepository
import fr.uge.pokedex.database.PokedexAppDatabase
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

                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {

                    val currentBackStackEntry by navController.currentBackStackEntryAsState()
                    val currentRoute = currentBackStackEntry?.destination?.route

                    Scaffold(bottomBar = { if(currentRoute != Route.Profiles.path) BottomNavigationMenu(navController)},
                        topBar = { if(currentRoute != Route.Profiles.path) TopBar(navController)  }) {
                        Log.d("Padding",it.toString())
                        NavigationGraph(navController = navController)
                    }
                }

            }
        }
    }
}