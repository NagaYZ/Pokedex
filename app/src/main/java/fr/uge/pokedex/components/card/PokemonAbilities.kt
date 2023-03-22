package fr.uge.pokedex.components.card

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import fr.uge.pokedex.data.pokedex.PokedexStorageService
import fr.uge.pokedex.data.pokedex.pokemon.Abilities

@Composable
fun PokemonAbilities(abilities: Abilities) {
    Column(
        verticalArrangement = Arrangement.spacedBy(15.dp)
    ) {
        Text(text = "Abilities", style = MaterialTheme.typography.h5, fontWeight = FontWeight.Bold)
        Divider()
        val firstAbility = PokedexStorageService.getAbility(abilities.idFirst!!)!!
        TextDisplay(
            title = firstAbility.name,
            content = firstAbility.flavorText
        )
        Divider()
        if (abilities.idSecond != null) {
            val secondAbility = PokedexStorageService.getAbility(abilities.idFirst!!)!!
            TextDisplay(
                title = secondAbility.name,
                content = secondAbility.flavorText
            )
            Divider()
        }
        if (abilities.hiddenId != null) {
            val hiddenAbility = PokedexStorageService.getAbility(abilities.hiddenId!!)!!
            TextDisplay(
                title = hiddenAbility.name,
                content = hiddenAbility.flavorText
            )
            Divider()
        }
    }
}