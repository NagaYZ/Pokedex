package fr.uge.pokedex.components.card

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import fr.uge.pokedex.data.pokedex.Version

@Composable
fun PokedexEntries(pokedexEntries: MutableMap<Version, String>) {
    Column(
        Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Text(
            text = "Pokédex Entries", style = MaterialTheme.typography.h5,
            fontWeight = FontWeight.Bold
        )
        Divider()
        for (pokedexEntry in pokedexEntries.map { it.value to it.key }
            .groupBy({ it.first }, { it.second }).entries.sortedBy { it.value.first().ordinal }) {
            TextDisplay(
                title = pokedexEntry.value.joinToString(" - "),
                content = pokedexEntry.key
            )
            Divider()
        }
    }
}