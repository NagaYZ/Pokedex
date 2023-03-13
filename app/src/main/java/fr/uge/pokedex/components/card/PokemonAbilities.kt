package fr.uge.pokedex.components.card

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import fr.uge.pokedex.data.pokedex.Abilities

@Composable
fun PokemonAbilities(abilities: Abilities) {
    Column(
        verticalArrangement = Arrangement.spacedBy(15.dp)
    ) {
        Text(text = "Abilities", style = MaterialTheme.typography.h5, fontWeight = FontWeight.Bold)
        Divider()
        TextDisplay(title = abilities.first!!.name, content = abilities.first!!.flavorText)
        Divider()
        if (abilities.second != null) {
            TextDisplay(title = abilities.second!!.name, content = abilities.second!!.flavorText)
            Divider()
        }
        if (abilities.hidden != null) {
            TextDisplay(title = abilities.hidden!!.name, content = abilities.hidden!!.flavorText)
            Divider()
        }
    }
}