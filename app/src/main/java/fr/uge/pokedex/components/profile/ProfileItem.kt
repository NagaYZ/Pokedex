package fr.uge.pokedex.components.profile

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import fr.uge.pokedex.R
import fr.uge.pokedex.components.navigation.Route
import fr.uge.pokedex.data.user.Profile
import fr.uge.pokedex.theme.Purple200
import fr.uge.pokedex.theme.Purple400
import fr.uge.pokedex.theme.Purple500
import fr.uge.pokedex.theme.Shapes

val profilePics = listOf(
    R.drawable.pikachu,
    R.drawable.bulbasaur,
    R.drawable.charmander,
    R.drawable.squirtle,
    R.drawable.caterpie,
    R.drawable.bellsprout,
    R.drawable.nidoran_f,
    R.drawable.nidoran_m,
    R.drawable.weedle,
    R.drawable.pidgey,
    R.drawable.meowth,
    R.drawable.psyduck,
    R.drawable.jigglypuff,
    R.drawable.sandshrew,
    R.drawable.vulpix,
)

@Composable
fun ProfileItem(
    profile: Profile,
    navController: NavHostController,
    onDeleteProfile: (profile: Profile) -> Unit,
    onEditProfile: (profile: Profile) -> Unit,
    setCurrentProfile: (profileId: Long) -> Unit
) {
    Row(
        modifier = Modifier
            .width(300.dp)
            .background(
                color = Purple400, shape = MaterialTheme.shapes.medium
            )
            .clickable(onClick = {
                setCurrentProfile.invoke(profile.getId())
                navController.navigate(Route.Pokedex.path)
            })
            .padding(10.dp)
        ,
        verticalAlignment = Alignment.CenterVertically
    ) {

        Row(
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ){
            Image(
                bitmap = ImageBitmap.imageResource(
                    profilePics[profile.getId().toInt() * 34 % profilePics.size]
                ),
                contentDescription = "Pokemon sprite",
                modifier = Modifier
                    .size(50.dp)
                    .border(
                        width = 2.dp,
                        color = Color(0x92FFFFFF),
                        shape = MaterialTheme.shapes.medium
                    )
                    .clip(shape = MaterialTheme.shapes.medium),
                filterQuality = FilterQuality.None,
            )
            Text(
                text = profile.getProfileName().padEnd(10, ' ')
                    .slice(0..9),
                style = MaterialTheme.typography.subtitle2,
                color = Color.White,
                modifier = Modifier
                    .padding(horizontal = 14.dp),
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )
        }
        
        Row(
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Button(
                onClick = { onDeleteProfile.invoke(profile) },
                // Uses ButtonDefaults.ContentPadding by default
                contentPadding = PaddingValues(12.dp),
                modifier = Modifier.size(40.dp)
            ) {
                // Inner content including an icon and a text label
                Icon(
                    Icons.Filled.Delete,
                    contentDescription = "Delete",
                    modifier = Modifier.size(ButtonDefaults.IconSize)
                )
            }
            Spacer(modifier = Modifier.width(12.dp))
            Button(
                onClick = { onEditProfile.invoke(profile) },
                // Uses ButtonDefaults.ContentPadding by default
                contentPadding = PaddingValues(12.dp),
                modifier = Modifier.size(40.dp)
            ) {
                // Inner content including an icon and a text label
                Icon(
                    Icons.Filled.Edit,
                    contentDescription = "Edit",
                    modifier = Modifier.size(ButtonDefaults.IconSize)
                )
            }
            Spacer(modifier = Modifier.width(4.dp))
        }
    }
}