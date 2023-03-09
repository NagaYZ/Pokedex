package fr.uge.pokedex.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.capitalize
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.toUpperCase
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import fr.uge.pokedex.data.*
import fr.uge.pokedex.ui.theme.PokedexTheme
import java.util.*

@Preview
@Composable
fun PokemonInfoDisplay(
    pokemon: Pokemon = Pokemon(
        id = 1,
        height = 7,
        weight = 69,
        genus = "Seed pokemon",
        eggGroups = mutableSetOf(EggGroup.PLANT, EggGroup.MONSTER),
        baseExperience = 64,
        growRate = GrowRate.MEDIUM_SLOW,
        baseHappiness = 50,
        captureRate = 45,
        hatchCounter = 20,
        abilities = Abilities(
            first = Ability(
                id = 1,
                name = "Overgrow",
                flavorText = "Overgrow increases the power of Grass-type moves by 50% (1.5×) when " +
                        "the ability-bearer's HP falls below a third of its maximum (known " +
                        "in-game as in a pinch)."
            ),
            hidden = Ability(
                id = 2,
                name = "Chlorophyll",
                flavorText = "Chlorophyll doubles the ability-bearer's Speed during bright sunshine."
            )
        ),
        identifier = "bulbasaur",
        name = "Bulbasaur"
    )
) {
    PokedexTheme() {
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .padding(30.dp),
            verticalArrangement = Arrangement.spacedBy(30.dp)
        ) {
            item {
                PokemonCharacteristics(pokemon)
            }
            item {
                PokemonAbilities(pokemon.abilities)
            }
        }
    }
}

@Composable
private fun PokemonAbilities(abilities: Abilities) {
    Column(
        verticalArrangement = Arrangement.spacedBy(15.dp)
    ) {
        Text(text = "Abilities", style = MaterialTheme.typography.h5, fontWeight = FontWeight.Bold)
        Divider()
        TextDisplay(title = abilities.first!!.name, content = abilities.first!!.flavorText)
        Divider()
        if(abilities.second != null) {
            TextDisplay(title = abilities.second!!.name, content = abilities.second!!.flavorText)
            Divider()
        }
        if(abilities.hidden != null) {
            TextDisplay(title = abilities.hidden!!.name, content = abilities.hidden!!.flavorText)
            Divider()
        }
    }
}

@Composable
private fun PokemonCharacteristics(pokemon: Pokemon) {
    Column(
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Text(text = "Characteristics", style = MaterialTheme.typography.h5,
            fontWeight = FontWeight.Bold)
        Divider()
        InfoDisplay(
            icon = fr.uge.pokedex.R.drawable.phylogenetic, title = "Species",
            content = pokemon.genus
        )
        Divider()
        InfoDisplay(
            icon = fr.uge.pokedex.R.drawable.text_height, title = "Height",
            content = "${pokemon.height.toFloat() / 10} m"
        )
        Divider()
        InfoDisplay(
            icon = fr.uge.pokedex.R.drawable.weight, title = "Weight",
            content = "${pokemon.weight.toFloat() / 10} kg"
        )
        Divider()
        InfoDisplay(
            icon = fr.uge.pokedex.R.drawable.arrows, title = "Grow Rate",
            content = pokemon.growRate.toString()
        )
        Divider()
        InfoDisplay(
            icon = fr.uge.pokedex.R.drawable.dumbbel, title = "Base Exp.",
            content = pokemon.baseExperience.toString()
        )
        Divider()
        InfoDisplay(
            icon = fr.uge.pokedex.R.drawable.pokeball, title = "Capture Rate",
            content = pokemon.captureRate.toString()
        )
        Divider()
        InfoDisplay(
            icon = fr.uge.pokedex.R.drawable.happiness, title = "Base Happiness",
            content = pokemon.baseHappiness.toString()
        )
        Divider()
        InfoDisplay(
            icon = fr.uge.pokedex.R.drawable.eggs, title = "Egg Groups",
            content = pokemon.eggGroups.joinToString(separator = ", ")
        )
        Divider()
        InfoDisplay(
            icon = fr.uge.pokedex.R.drawable.footsteps_silhouette_variant, title = "Egg Cycles",
            content = pokemon.hatchCounter.toString()
        )
        Divider()
    }
}

@Composable
private fun InfoDisplay(icon: Int, title: String, content: String) {
    Row(Modifier.fillMaxWidth()) {
        Icon(
            painter = painterResource(id = icon),
            contentDescription = "$title icon",
            modifier = Modifier
                .size(35.dp, 35.dp)
                .padding(5.dp),
            tint = Color.LightGray
        )
        Spacer(modifier = Modifier.width(17.dp))
        TextDisplay(title = title , content = content)
    }
}

@Composable
private fun TextDisplay(title: String, content: String) {
    Column(verticalArrangement = Arrangement.Center) {
        Text(
            text = title, style = MaterialTheme.typography.body1,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(2.dp))
        Text(text = content, style = MaterialTheme.typography.body1)
    }
}