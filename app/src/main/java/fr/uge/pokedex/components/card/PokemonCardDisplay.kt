package fr.uge.pokedex.components.card

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import fr.uge.pokedex.R
import fr.uge.pokedex.components.list.PokemonBoxDisplay
import fr.uge.pokedex.data.pokedex.*
import fr.uge.pokedex.data.user.Favorite

@Preview
@Composable
fun PokemonCardDisplay(
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
        evolutionChain = EvolutionChain(
            evolutions = mutableListOf(
                Evolution(
                    species = Pokemon(1, "bulbasaur", icon = R.drawable.icon_pkm_1, sprite = R.drawable.pokemon_1),
                    evolvedSpecies = Pokemon(2, "ivysaur", icon = R.drawable.icon_pkm_2, sprite = R.drawable.pokemon_2),
                ),
                Evolution(
                    species = Pokemon(2, "ivysaur", icon = R.drawable.icon_pkm_2, sprite = R.drawable.pokemon_2),
                    evolvedSpecies = Pokemon(3, "venusaur", icon = R.drawable.icon_pkm_3, sprite = R.drawable.pokemon_3),
                )
            )
        ),
        identifier = "bulbasaur",
        name = "Bulbasaur",
        pokedexEntries = hashMapOf(
            Version.RED to "A strange seed was planted on its back at birth. The plant sprouts and grows with this POKéMON.",
            Version.BLUE to "A strange seed was planted on its back at birth. The plant sprouts and grows with this POKéMON.",
            Version.YELLOW to "It can go for days without eating a single morsel. In the bulb on its back, it stores energy.",
            Version.GOLD to "The seed on its back is filled with nutrients. The seed grows steadily larger as its body grows."
        ),
        icon = R.drawable.icon_pkm_1,
        sprite = R.drawable.pokemon_1,
        baseStats = BaseStats(
            hp = 45,
            attack = 49,
            defense = 49,
            specialAttack = 65,
            specialDefense = 65,
            speed = 45
        )
    ),
    onClickFavorite: (Long, Favorite?) -> Unit = {pokemonId:Long, favorite:Favorite? ->},
    favoriteList: List<Favorite> = emptyList()
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize(),
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
            EvolutionChainDisplay(pokemon.evolutionChain)
        }
        item {
            BaseStatsDisplay(pokemon.baseStats)
        }
        item {
            PokedexEntries(pokemon.pokedexEntries)
        }
        item {
            Spacer(modifier = Modifier.height(50.dp))
        }
    }

}