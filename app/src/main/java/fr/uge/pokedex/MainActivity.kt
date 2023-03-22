package fr.uge.pokedex


import android.content.Intent
import android.content.IntentFilter
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
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.core.view.accessibility.AccessibilityEventCompat.setAction
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import fr.uge.pokedex.bluetooth.PokedexReceiver
import fr.uge.pokedex.components.navigation.BottomNavigationMenu
import fr.uge.pokedex.components.navigation.NavigationGraph
import fr.uge.pokedex.components.navigation.Route
import fr.uge.pokedex.components.profile.TopBar
import fr.uge.pokedex.data.pokedex.Pokemon
import fr.uge.pokedex.data.pokedex.PokemonRepository
import fr.uge.pokedex.theme.PokedexTheme


class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val receiver = PokedexReceiver()
        val intentFilter=
            IntentFilter()

        intentFilter.apply {
            addAction("teamCreated")
            addAction("teamEdited")
            addAction("teamDeleted")
            addAction("favoriteAdded")
            addAction("favoriteDeleted")
        }

        registerReceiver(receiver, intentFilter)

        setContent {
            PokedexTheme {
                val navController: NavHostController = rememberNavController()

                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    var pokemonMap by rememberSaveable { mutableStateOf(mapOf<Long, Pokemon>()) }
                    var dataLoaded by rememberSaveable { mutableStateOf(false) }

                    if(!dataLoaded){
                        LaunchedEffect(true) {
                            val pokemonRepository = PokemonRepository(applicationContext)
                            pokemonMap = pokemonRepository.data
                            dataLoaded = true
                        }
                    }

                    val currentBackStackEntry by navController.currentBackStackEntryAsState()
                    val currentRoute = currentBackStackEntry?.destination?.route

                    var currentProfileId by rememberSaveable { mutableStateOf(-1L) }

                    Scaffold(bottomBar = {
                        if (currentRoute != Route.Profiles.path) BottomNavigationMenu(
                            navController
                        )
                    },
                        topBar = {
                            if (currentRoute != Route.Profiles.path) TopBar(
                                navController,
                                currentProfileId
                            )
                        }) {
                        it
                        NavigationGraph(
                            applicationContext = applicationContext,
                            navController = navController,
                            setCurrentProfile = { profileId: Long -> currentProfileId = profileId },
                            profileId = currentProfileId,
                            pokemonMap = pokemonMap
                        )
                    }
                }

            }
        }
    }
}