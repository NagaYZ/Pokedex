package fr.uge.pokedex


import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import fr.uge.pokedex.components.navigation.BottomNavigationMenu
import fr.uge.pokedex.components.navigation.NavigationGraph
import fr.uge.pokedex.components.navigation.Route
import fr.uge.pokedex.components.profile.TopBar
import fr.uge.pokedex.data.pokedex.PokemonRepository
import fr.uge.pokedex.data.user.PokedexAppDatabaseConnection
import fr.uge.pokedex.data.user.Profile
import fr.uge.pokedex.theme.PokedexTheme

class MainActivity : ComponentActivity() {
    private lateinit var pokemonRepository: PokemonRepository


    @RequiresApi(Build.VERSION_CODES.N)
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

                    var currentProfile by remember { mutableStateOf(Profile("")) }

                    Scaffold(bottomBar = {
                        if (currentRoute != Route.Profiles.path) BottomNavigationMenu(
                            navController
                        )
                    },
                        topBar = {
                            if (currentRoute != Route.Profiles.path) TopBar(
                                navController,
                                currentProfile
                            )
                        }) {
                        it
                        NavigationGraph(
                            applicationContext = applicationContext,
                            navController = navController,
                            setCurrentProfile = { profile: Profile -> currentProfile = profile },
                            profile = currentProfile,
                            pokemonMap = pokemonRepository.data
                        )
                    }
                }

            }
        }
    }
}