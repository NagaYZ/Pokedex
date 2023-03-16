package fr.uge.pokedex.components.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import fr.uge.pokedex.theme.Shapes

@Composable
fun ProfileDialog(
    show: Boolean,
    close: () -> Unit,
    onProfileNameAccept: (profileName: String) -> Unit
) {
    if (!show) return

    Dialog(onDismissRequest = { close.invoke() }) {

        Column(
            modifier = Modifier
                .background(MaterialTheme.colors.background, Shapes.medium)
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            var profileName by remember { mutableStateOf("") }

            Text(text = "Please enter a new profile name", style = MaterialTheme.typography.h6)
            Spacer(modifier = Modifier.padding(5.dp))
            TextField(value = profileName, onValueChange = {
                if (!it.contains(Regex("[\\n\\t]"))) profileName = it
            })
            Spacer(modifier = Modifier.padding(5.dp))
            Button(onClick = {
                onProfileNameAccept.invoke(profileName)
                close.invoke()
            }) {
                Text(text = "Confirm", style = MaterialTheme.typography.button)
            }
        }

    }
}