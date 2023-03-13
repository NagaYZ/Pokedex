package fr.uge.pokedex.components.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.window.Dialog

@Composable
fun ProfileDialog(
    show: Boolean,
    close: () -> Unit,
    onProfileNameAccept: (profileName: String) -> Unit
) {
    if (!show) return

    Dialog(onDismissRequest = { close.invoke() }) {

        Column(
            modifier = Modifier.background(MaterialTheme.colors.background),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            var profileName by remember { mutableStateOf("") }

            Text(text = "Please enter new profile name", style = MaterialTheme.typography.h6)
            TextField(value = profileName, onValueChange = {
                profileName = it
            })
            Button(onClick = {
                onProfileNameAccept.invoke(profileName)
                close.invoke()
            }) {
                Text(text = "Confirm", style = MaterialTheme.typography.button)
            }
        }

    }
}