package fr.uge.pokedex.components.team

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import fr.uge.pokedex.components.list.TypeDisplay
import fr.uge.pokedex.data.pokedex.Type

@Preview
@Composable
fun PokemonTypeTeamDisplay(type: Pair<Type, Type> = Pair(Type.ELECTRIC, Type.DRAGON)) {
    Column() {
        TypeDisplay(type.first)
        Spacer(modifier = Modifier.height(3.dp))
        TypeDisplay(type.second)
    }
}