package fr.uge.pokedex


import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.*
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import fr.uge.pokedex.components.BottomNavigationMenu
import fr.uge.pokedex.components.NavigationGraph
import fr.uge.pokedex.ui.theme.PokedexTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PokedexTheme {

                val navController: NavHostController = rememberNavController()

                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {

                    Scaffold(bottomBar = { BottomNavigationMenu(navController)}) {
                        Log.d("Padding",it.toString())
                        NavigationGraph(navController = navController)
                    }
                }
                
            }
        }
    }
}