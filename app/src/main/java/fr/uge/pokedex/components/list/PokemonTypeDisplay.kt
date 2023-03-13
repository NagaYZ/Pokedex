package fr.uge.pokedex.components.list

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import fr.uge.pokedex.data.pokedex.Type

@Preview
@Composable
fun PokemonTypeDisplay(type: Pair<Type, Type> = Pair(Type.ELECTRIC, Type.DRAGON)) {
    Row() {
        TypeDisplay(type.first)
        Spacer(modifier = Modifier.width(3.dp))
        TypeDisplay(type.second)
    }
}