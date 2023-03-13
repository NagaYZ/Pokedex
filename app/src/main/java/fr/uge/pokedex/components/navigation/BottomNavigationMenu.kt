package fr.uge.pokedex.components.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController

@Composable
fun BottomNavigationMenu(navController: NavHostController) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = MaterialTheme.colors.primary)
            .clickable(false) {  }, // Prevent click through
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Spacer(modifier = Modifier.weight(0.3f))
        BottomMenuButton(
            route = Route.Pokedex,
            navController = navController,
            icon = Icons.Filled.Menu,
            modifier = Modifier.weight(1f)
        )
        BottomMenuButton(
            route = Route.Favorite,
            navController = navController,
            icon = Icons.Filled.Favorite,
            modifier = Modifier.weight(1f)
        )
        BottomMenuButton(
            route = Route.Teams,
            navController = navController,
            icon = Icons.Filled.Add,
            modifier = Modifier.weight(1f)
        )
        Spacer(modifier = Modifier.weight(0.3f))
    }
}