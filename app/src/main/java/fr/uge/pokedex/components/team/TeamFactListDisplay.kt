package fr.uge.pokedex.components.team

import androidx.compose.foundation.layout.*
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Info
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import fr.uge.pokedex.service.team.FactCategory
import fr.uge.pokedex.service.team.TeamFact

@Preview
@Composable
fun TeamFactListDisplay(
    teamFactList: List<TeamFact> = listOf(
        TeamFact.WEAKNESS_TO_TYPES.setMessage("FIRE, WATER, GRASS"),
        TeamFact.RESISTANCE_TO_TYPES.setMessage("STEEL, FIGHTING, GHOST"),
        TeamFact.GOOD_TYPE_COVERAGE,
        TeamFact.LOW_BASE_STATS.setMessage("${234}"),
        TeamFact.HIGH_BASE_STATS.setMessage("${556}"),
        TeamFact.BALANCED_BASE_STATS.setMessage("${15}"),
        TeamFact.EMPTY_SLOTS,
    )
) {
    val strengthTeamFacts = teamFactList.filter { it.category == FactCategory.STRENGTH }
    val weaknessTeamFacts = teamFactList.filter { it.category == FactCategory.WEAKNESS }
    val infoTeamFacts = teamFactList.filter { it.category == FactCategory.INFO }

    Column(
        Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        if(strengthTeamFacts.isNotEmpty()) {
            TeamFactHeader(Icons.Default.Done, Color(0xFF9AE970), "Strengths")
            Divider()
            for (teamFact in strengthTeamFacts) {
                TeamFactDisplay(teamFact)
                Divider()
            }
        }

        if(weaknessTeamFacts.isNotEmpty()) {
            Spacer(modifier = Modifier.height(10.dp))
            TeamFactHeader(Icons.Default.Clear, Color(0xFFE97070), "Weaknesses")
            Divider()
            for (teamFact in weaknessTeamFacts) {
                TeamFactDisplay(teamFact)
                Divider()
            }
        }

        if(infoTeamFacts.isNotEmpty()) {
            Spacer(modifier = Modifier.height(10.dp))
            TeamFactHeader(Icons.Default.Info, Color(0xFF8A8A8A), "Info")
            Divider()
            for (teamFact in infoTeamFacts) {
                TeamFactDisplay(teamFact)
                Divider()
            }
        }
    }
}

@Composable
private fun TeamFactHeader(imageVector: ImageVector, iconTint: Color, title: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = imageVector,
            tint = iconTint,
            contentDescription = "header icon",
            modifier = Modifier.padding(5.dp)
        )
        Text(
            text = title, style = MaterialTheme.typography.h5,
            fontWeight = FontWeight.Bold
        )
    }
}