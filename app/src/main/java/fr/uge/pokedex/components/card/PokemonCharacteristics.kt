package fr.uge.pokedex.components.card

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import fr.uge.pokedex.R
import fr.uge.pokedex.data.pokedex.Pokemon

@Composable
fun PokemonCharacteristics(pokemon: Pokemon) {
    Column(
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Text(
            text = "Characteristics", style = MaterialTheme.typography.h5,
            fontWeight = FontWeight.Bold
        )
        Divider()
        TextDisplay(
            iconResource = R.drawable.phylogenetic, title = "Species",
            content = pokemon.genus
        )
        Divider()
        TextDisplay(
            iconResource = R.drawable.text_height, title = "Height",
            content = "${pokemon.height.toFloat() / 10} m"
        )
        Divider()
        TextDisplay(
            iconResource = R.drawable.weight, title = "Weight",
            content = "${pokemon.weight.toFloat() / 10} kg"
        )
        Divider()
        TextDisplay(
            iconResource = R.drawable.arrows, title = "Grow Rate",
            content = pokemon.growRate.toString()
        )
        Divider()
        TextDisplay(
            iconResource = R.drawable.dumbbel, title = "Base Exp.",
            content = pokemon.baseExperience.toString()
        )
        Divider()
        TextDisplay(
            iconResource = R.drawable.pokeball, title = "Capture Rate",
            content = pokemon.captureRate.toString()
        )
        Divider()
        TextDisplay(
            iconResource = R.drawable.happiness, title = "Base Happiness",
            content = pokemon.baseHappiness.toString()
        )
        Divider()
        TextDisplay(
            iconResource = R.drawable.eggs, title = "Egg Groups",
            content = pokemon.eggGroups.joinToString(separator = ", ")
        )
        Divider()
        TextDisplay(
            iconResource = R.drawable.footsteps_silhouette_variant, title = "Egg Cycles",
            content = pokemon.hatchCounter.toString()
        )
        Divider()
    }
}