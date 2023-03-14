package fr.uge.pokedex.components.team

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import fr.uge.pokedex.components.pokedex.PokedexDisplay
import fr.uge.pokedex.data.pokedex.Pokemon
import fr.uge.pokedex.data.user.Profile
import fr.uge.pokedex.theme.Purple200

@Composable
fun PokemonSelection(
    pokemonMap: Map<Long, Pokemon>,
    profile: Profile,
    edit: Boolean,
    pokemonIdInTeam: Long,
    getPokemonId: (Long) -> Unit
) {
    var showPokemonList by remember { mutableStateOf(false) }
    var currentPokemon by remember { mutableStateOf(-1L) }

    if (edit) {
        currentPokemon = pokemonIdInTeam
    }

    Box(
        modifier = Modifier
            .padding(4.dp)
            .height(70.dp)
            .fillMaxWidth()
            .background(Purple200, RoundedCornerShape(8.dp))
            .clickable {
                showPokemonList = true
            },
        contentAlignment = Alignment.Center
    ) {
        Text("Choose Pokemon...", fontSize = 20.sp, textAlign = TextAlign.Center)
        pokemonMap[currentPokemon]?.let {
            DisplaySelectedPokemon(it) { showPokemonList = true }
            getPokemonId(it.id)
        }
    }

    //show pokedex to select pokemon
    if (showPokemonList) {
        PokedexDisplay(pokemonList = pokemonMap.values.toList(), profile = profile, clickFavorite = { _, _ ->  },
        onClick = { pokemonId ->
            currentPokemon = pokemonId
            showPokemonList = false
        })
    }
}