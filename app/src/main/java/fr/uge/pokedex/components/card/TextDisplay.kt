package fr.uge.pokedex.components.card

import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun TextDisplay(title: String, content: String, iconResource: Int? = null) {
    Row(Modifier.fillMaxWidth()) {
        if (iconResource != null) {
            Icon(
                painter = painterResource(id = iconResource),
                contentDescription = "$title icon",
                modifier = Modifier
                    .size(35.dp, 35.dp)
                    .padding(5.dp),
                tint = Color.LightGray
            )
            Spacer(modifier = Modifier.width(17.dp))
        }
        Column(verticalArrangement = Arrangement.Center) {
            Text(
                text = title, style = MaterialTheme.typography.body1,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(text = content, style = MaterialTheme.typography.body1)
        }
    }
}