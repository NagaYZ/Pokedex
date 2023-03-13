package fr.uge.pokedex.components.navigation

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState

@Composable
fun BottomMenuButton(
    route: Route,
    navController: NavHostController,
    icon: ImageVector,
    modifier: Modifier = Modifier
) {

    val currentBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = currentBackStackEntry?.destination?.route

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceEvenly,
        modifier = modifier.clickable {
            navController.navigate(route.path)
        }.padding(7.dp)
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
                color = Color.White
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