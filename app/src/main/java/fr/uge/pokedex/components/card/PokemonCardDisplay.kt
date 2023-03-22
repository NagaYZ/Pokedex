package fr.uge.pokedex.components.card

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import fr.uge.pokedex.R
import fr.uge.pokedex.components.list.PokemonBoxDisplay
import fr.uge.pokedex.data.pokedex.PokedexStorageService
import fr.uge.pokedex.data.pokedex.pokemon.*
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
            idFirst = 1,
            hiddenId = 2
        ),
        evolutionChainId = 1,
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
        if(pokemon.evolutionChainId != null) {
            item {
                EvolutionChainDisplay(PokedexStorageService
                    .getEvolutionChain(pokemon.evolutionChainId!!)!!)
            }
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