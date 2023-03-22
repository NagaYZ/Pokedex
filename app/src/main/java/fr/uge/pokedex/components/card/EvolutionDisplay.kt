package fr.uge.pokedex.components.card

import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import fr.uge.pokedex.components.list.PokemonIcon
import fr.uge.pokedex.data.pokedex.PokedexStorageService
import fr.uge.pokedex.data.pokedex.evolution.Evolution
import fr.uge.pokedex.data.pokedex.evolution.EvolutionTrigger

@Composable
fun EvolutionDisplay(evolution: Evolution) {
    Column(
        Modifier
            .fillMaxWidth()
    ) {
        Text(
            text = if (evolution.evolutionTrigger == EvolutionTrigger.LEVEL_UP) {
                "Level ${evolution.minimumLevel ?: "up + Trigger"}"
            } else {
                evolution.evolutionTrigger.toString()
            },
            style = MaterialTheme.typography.subtitle1,
            fontWeight = FontWeight.Bold
        )
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .padding(20.dp)
                .fillMaxWidth()
        ) {
            PokemonIcon(
                iconResource = PokedexStorageService
                    .getPokemon(evolution.evolvesFromSpeciesId)?.icon!!,
                modifier = Modifier.size(60.dp)
            )
            Icon(
                imageVector = Icons.Default.ArrowForward,
                contentDescription = "Right Icon",
                tint = Color.LightGray
            )
            PokemonIcon(
                iconResource = PokedexStorageService
                    .getPokemon(evolution.speciesId)?.icon!!,
                modifier = Modifier.size(60.dp)
            )
        }
    }
}