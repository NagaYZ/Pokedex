package fr.uge.pokedex

import android.content.*
import android.os.Build
import android.os.Bundle
import android.os.IBinder
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import fr.uge.pokedex.broadcastReceiver.PokedexReceiver
import fr.uge.pokedex.components.navigation.BottomNavigationMenu
import fr.uge.pokedex.components.navigation.NavigationGraph
import fr.uge.pokedex.components.navigation.Route
import fr.uge.pokedex.components.profile.TopBar
import fr.uge.pokedex.data.pokedex.PokedexStorageService
import fr.uge.pokedex.service.PokemonMusicService
import fr.uge.pokedex.theme.PokedexTheme

class MainActivity : ComponentActivity() {
    private lateinit var pokemonMusicService: PokemonMusicService
    private var serviceBound: Boolean = false

    override fun onStart() {
        super.onStart()
        // Bind to the service.
        Intent(this, PokemonMusicService::class.java).also { intent ->
            bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE)
        }
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        PokedexStorageService.load(applicationContext)
    }

    private val serviceConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName, service: IBinder) {
            val binder: PokemonMusicService.LocalBinder = service as PokemonMusicService.LocalBinder
            pokemonMusicService = binder.getService()
            serviceBound = true
            setContent {
                PokedexTheme {
                    PokedexApp(pokemonMusicService)
                }
            }
        }

        override fun onServiceDisconnected(name: ComponentName) {
            serviceBound = false
        }
    }

    override fun onStop() {
        super.onStop()
        // Unbind from the service.
        if (serviceBound) {
            unbindService(serviceConnection)
            serviceBound = false
        }
    }

    @Composable
    fun PokedexApp(pokemonMusicService: PokemonMusicService) {
        var audioState by remember {
            mutableStateOf(true)
        }

        val receiver = PokedexReceiver()
        val intentFilter =
            IntentFilter()

        intentFilter.apply {
            addAction("profileCreated")
            addAction("profileEdited")
            addAction("profileDeleted")
            addAction("teamCreated")
            addAction("teamEdited")
            addAction("teamDeleted")
            addAction("favoriteAdded")
            addAction("favoriteDeleted")
        }

        registerReceiver(receiver, intentFilter)
        val navController: NavHostController = rememberNavController()
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colors.background
        ) {
            val currentBackStackEntry by navController.currentBackStackEntryAsState()
            val currentRoute = currentBackStackEntry?.destination?.route
            var currentProfileId by rememberSaveable { mutableStateOf(-1L) }

            var showBars by rememberSaveable { mutableStateOf(false) }

            LaunchedEffect(key1 = currentRoute) {
                showBars = currentRoute != Route.Profiles.path
            }

            Scaffold(bottomBar = {
                AnimatedVisibility(
                    enter = slideInVertically(initialOffsetY = { it }),
                    exit = slideOutVertically(targetOffsetY = { it }),
                    visible = showBars,
                ) {
                    BottomNavigationMenu(navController)
                }
            },
                topBar = {
                    AnimatedVisibility(
                        enter = slideInVertically(initialOffsetY = { -it }),
                        exit = slideOutVertically(targetOffsetY = { -it }),
                        visible = showBars,
                    ) {
                        TopBar(
                            navController,
                            currentProfileId,
                            {
                                if (audioState) {
                                    pokemonMusicService.getMediaPlayer().pause()
                                } else {
                                    pokemonMusicService.getMediaPlayer().start()
                                }
                                audioState = !audioState
                            },
                            audioState
                        )
                    }
                }) {
                it
                NavigationGraph(
                    applicationContext = applicationContext,
                    navController = navController,
                    setCurrentProfile = { profileId: Long ->
                        currentProfileId = profileId
                    },
                    profileId = currentProfileId,
                    {
                        if (audioState) {
                            pokemonMusicService.getMediaPlayer().pause()
                        } else {
                            pokemonMusicService.getMediaPlayer().start()
                        }
                        audioState = !audioState
                    },
                    audioState
                )
            }
        }
    }
}
