package fr.uge.pokedex.components.team

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import fr.uge.pokedex.components.list.PokemonIcon
import fr.uge.pokedex.data.pokedex.Pokemon

@Composable
fun PokemonTeamCard(
    pokemon: Pokemon,
    onClick: (Long) -> Unit
) {
    Column(
        Modifier
            .padding(4.dp)
            .fillMaxHeight()
            .background(Color(0x85FFFFFF), RoundedCornerShape(4.dp))
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .padding(4.dp)
                .fillMaxHeight()
                .weight(1f)
                .background(MaterialTheme.colors.background, RoundedCornerShape(4.dp))
                .padding(10.dp)
                .clickable { onClick(pokemon.id) }
        ) {
            PokemonIcon(pokemon.icon, modifier = Modifier.size(60.dp))
            Text(
                text = pokemon.name,
                fontSize = 14.sp,
                modifier = Modifier.padding(horizontal = 3.dp),
                style = MaterialTheme.typography.h3
            )
            PokemonTypeTeamDisplay(type = pokemon.type)
        }
    }
}