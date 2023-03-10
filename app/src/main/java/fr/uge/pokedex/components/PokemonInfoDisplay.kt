package fr.uge.pokedex.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import fr.uge.pokedex.data.*
import fr.uge.pokedex.database.Favorite

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
        evolvesFrom = null,
        evolvesInto = mutableSetOf(
            Evolution(
                species = Pokemon(
                    1,
                    "bulbasaur",
                    icon = fr.uge.pokedex.R.drawable.icon_pkm_1,
                    sprite = fr.uge.pokedex.R.drawable.pokemon_1
                ),
                evolutionTrigger = EvolutionTrigger.LEVEL_UP,
                minimumLevel = 16,
                evolvedSpecies = Pokemon(
                    2,
                    "ivysaur",
                    icon = fr.uge.pokedex.R.drawable.icon_pkm_2,
                    sprite = fr.uge.pokedex.R.drawable.pokemon_2,
                    evolvesInto = mutableSetOf(
                        Evolution(
                            species = Pokemon(
                                2,
                                "ivysaur",
                                icon = fr.uge.pokedex.R.drawable.icon_pkm_2,
                                sprite = fr.uge.pokedex.R.drawable.pokemon_2
                            ),
                            evolvedSpecies = Pokemon(
                                3,
                                "venusaur",
                                icon = fr.uge.pokedex.R.drawable.icon_pkm_3,
                                sprite = fr.uge.pokedex.R.drawable.pokemon_3
                            ),
                            evolutionTrigger = EvolutionTrigger.LEVEL_UP,
                            minimumLevel = 32
                        )
                    )
                ),
            )
        ),
        identifier = "bulbasaur",
        name = "Bulbasaur",
        pokedexEntries = mutableSetOf(
            FlavorText(
                "A strange seed was planted on its back at birth. The plant sprouts and grows with this POKéMON.",
                Version.RED
            ),
            FlavorText(
                "A strange seed was planted on its back at birth. The plant sprouts and grows with this POKéMON.",
                Version.BLUE
            ),
            FlavorText(
                "It can go for days without eating a single morsel. In the bulb on its back, it stores energy.",
                Version.YELLOW
            ),
            FlavorText(
                "The seed on its back is filled with nutrients. The seed grows steadily larger as its body grows.",
                Version.GOLD
            ),
        ),
        icon = fr.uge.pokedex.R.drawable.icon_pkm_1,
        sprite = fr.uge.pokedex.R.drawable.pokemon_1
    ),
    onClickFavorite: (Boolean) -> Unit = {},
    favoriteList: List<Long> = emptyList()
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(30.dp),
        contentPadding = PaddingValues(30.dp)
    ) {
        item {
            PokemonBoxDisplay(
                pokemon,
                favoriteList = favoriteList,
                onClickFavorite = onClickFavorite
            )
        }
        item {
            PokemonCharacteristics(pokemon)
        }
        item {
            PokemonAbilities(pokemon.abilities)
        }
        item {
            PokemonEvolutions(pokemon.evolvesFrom, pokemon.evolvesInto)
        }
        item {
            PokedexEntries(pokemon.pokedexEntries)
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

@Composable
private fun PokemonCharacteristics(pokemon: Pokemon) {
    Column(
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Text(
            text = "Characteristics", style = MaterialTheme.typography.h5,
            fontWeight = FontWeight.Bold
        )
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
        TextDisplay(title = title, content = content)
    }
}

@Composable
private fun PokedexEntries(pokedexEntries: MutableSet<FlavorText>) {
    Column(
        Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Text(
            text = "Pokédex Entries", style = MaterialTheme.typography.h5,
            fontWeight = FontWeight.Bold
        )
        Divider()
        for (pokedexEntry in pokedexEntries.sortedBy { it.version.ordinal }) {
            TextDisplay(title = pokedexEntry.version.toString(), content = pokedexEntry.description)
            Divider()
        }
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

@Composable
private fun PokemonEvolutions(evolvesFrom: Evolution?, evolvesInto: Set<Evolution>) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Text(
            text = "Evolutions", style = MaterialTheme.typography.h5,
            fontWeight = FontWeight.Bold
        )
        Divider()
        if (evolvesFrom != null) {
            EvolutionDisplay(evolvesFrom)
            Divider()
        }
        for (evolution in evolvesInto) {
            EvolutionDisplay(evolution)
            Divider()
            if (evolvesFrom == null) {
                for (finalEvolution in evolution.evolvedSpecies.evolvesInto) {
                    EvolutionDisplay(finalEvolution)
                    Divider()
                }
            }
        }
    }
}

@Composable
private fun EvolutionDisplay(evolution: Evolution) {
    Column(
        Modifier
            .fillMaxWidth()
    ) {
        Text(
            text = if (evolution.evolutionTrigger == EvolutionTrigger.LEVEL_UP) {
                "Level ${evolution.minimumLevel}"
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
            PokemonIcon(iconResource = evolution.species.icon)
            Icon(
                imageVector = Icons.Default.ArrowForward,
                contentDescription = "Right Icon",
                tint = Color.LightGray
            )
            PokemonIcon(iconResource = evolution.evolvedSpecies.icon)
        }
    }
}

@Composable
private fun PokemonIcon(iconResource: Int) {
    Image(
        painter = painterResource(id = iconResource),
        contentDescription = "Pokemon icon",
        modifier = Modifier
            .size(60.dp)
    )
}