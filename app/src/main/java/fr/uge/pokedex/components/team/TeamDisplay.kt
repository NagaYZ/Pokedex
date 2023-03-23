package fr.uge.pokedex.components.team

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.material.icons.rounded.Share
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import fr.uge.pokedex.data.pokedex.pokemon.Pokemon
import fr.uge.pokedex.data.user.TeamWithMembers
import fr.uge.pokedex.theme.Purple400

@Composable
fun TeamDisplay(
    index: Int,
    pokemon_team: TeamWithMembers,
    pokemonMap: Map<Long, Pokemon>,
    editOnClick: (Long) -> Unit,
    deleteOnClick: (Long) -> Unit,
    shareOnClick: (Long) -> Unit,
    showTeam: (Long) -> Unit,
    onPokemonClick: (Long) -> Unit,
) {
    Column(
        Modifier
            .background(Purple400, RoundedCornerShape(4.dp))
            .padding(8.dp)
    ) {
        Row(
            Modifier
                .height(50.dp)
                .padding(4.dp)
                .fillMaxWidth()
                .clickable { showTeam(pokemon_team.team.getTeamId()) },
            verticalAlignment = Alignment.CenterVertically

        ) {
            Text(
                if (pokemon_team.team.getTeamName() == "") {
                    "Team $index"
                } else {
                    pokemon_team.team.getTeamName()
                },
                Modifier
                    .weight(1f)
                    .clickable { showTeam(pokemon_team.team.getTeamId()) },
                style = TextStyle(fontSize = 24.sp),
                color = Color.White
            )
            Button(onClick = { editOnClick(pokemon_team.team.getTeamId()) }) {
                Icon(Icons.Rounded.Edit, "Edit Team")
            }
            Spacer(modifier = Modifier.width(2.dp))
            Button(onClick = { deleteOnClick(pokemon_team.team.getTeamId()) }) {
                Icon(Icons.Rounded.Delete, "Delete Team")
            }
            Button(onClick = { shareOnClick(pokemon_team.team.getTeamId()) }) {
                Icon(Icons.Rounded.Share, "Share Team")
            }
        }

        for (i in 0..1) {
            Row(
                Modifier
                    .height(IntrinsicSize.Max)
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                for (j in 0..2) {
                    val pokemon = pokemonMap[pokemon_team.teamMembers[i * 3 + j].getPokemonId()]
                    Box(Modifier.weight(1 / 3f)) {
                        if (pokemon != null) {
                            PokemonTeamCard(pokemon = pokemon, onPokemonClick)
                        }
                    }
                }
            }
        }
    }
}