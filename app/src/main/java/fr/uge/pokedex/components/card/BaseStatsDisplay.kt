package fr.uge.pokedex.components.card

import androidx.compose.foundation.layout.*
import androidx.compose.material.Divider
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import fr.uge.pokedex.data.pokedex.BaseStats
import fr.uge.pokedex.utils.BaseStatHelper.Companion.getAsList
import fr.uge.pokedex.utils.BaseStatHelper.Companion.getTotal

@Composable
@Preview
fun BaseStatsDisplay(
    baseStats: BaseStats = BaseStats(
        hp = 45,
        attack = 49,
        defense = 49,
        specialAttack = 65,
        specialDefense = 65,
        speed = 45
    )
) {
    Column(
        Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Text(
            text = "Base stats",
            style = MaterialTheme.typography.h5,
            fontWeight = FontWeight.Bold
        )
        Divider()
        for(baseStat in baseStats.getAsList()) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = baseStat.first,
                    style = MaterialTheme.typography.body1,
                    modifier = Modifier.width(70.dp)
                )
                Text(
                    text = "${baseStat.second}",
                    style = MaterialTheme.typography.body1,
                    modifier = Modifier.width(30.dp),
                    textAlign = TextAlign.End
                )
                Spacer(modifier = Modifier.width(10.dp))
                LinearProgressIndicator(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(8.dp)
                        .padding(horizontal = 10.dp),
                    progress = baseStat.second / 255f
                )
            }
            Divider()
        }
        Row {
            Text(
                text = "Total", style = MaterialTheme.typography.body1,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.width(70.dp)
            )
            Text(
                text = "${baseStats.getTotal()}", style = MaterialTheme.typography.body1,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.width(30.dp),
                textAlign = TextAlign.End
            )
        }
        Divider()
    }
}