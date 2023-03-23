package fr.uge.pokedex.components.team

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import fr.uge.pokedex.components.card.TextDisplay
import fr.uge.pokedex.service.team.TeamFact

@Preview
@Composable
fun TeamFactDisplay(
    teamFact: TeamFact = TeamFact.WEAKNESS_TO_TYPES.setMessage("FIRE, WATER, GRASS")
) {
    val teamFactContent = teamFactToTextContent(teamFact)
    TextDisplay(title = teamFactContent.first, content = teamFactContent.second)
}

private fun teamFactToTextContent(teamFact: TeamFact): Pair<String, String> {
    return when (teamFact) {
        TeamFact.WEAKNESS_TO_TYPES ->
            Pair(
                "Type weaknesses",
                "At least 3 of your members are weak to the following types: ${teamFact.getMessage()}"
            )
        TeamFact.RESISTANCE_TO_TYPES ->
            Pair(
                "Type resistances",
                "At least 3 of your members are resistant to the following types: ${teamFact.getMessage()}"
            )
        TeamFact.LOW_BASE_STATS -> Pair(
            "Low base stats",
            "Your team average total base stats is ${teamFact.getMessage()}"
        )
        TeamFact.HIGH_BASE_STATS -> Pair(
            "High base stats",
            "Your team average total base stats is ${teamFact.getMessage()}"
        )
        TeamFact.EMPTY_SLOTS -> Pair("Empty slots", "You're playing for fun")
        TeamFact.BALANCED_BASE_STATS -> Pair(
            "Balanced base stats",
            "Your team average base stats deviation is ${teamFact.getMessage()}"
        )
        TeamFact.IMBALANCED_BASE_STATS -> Pair(
            "Imbalanced base stats",
            "Your team average base stats deviation is ${teamFact.getMessage()}"
        )
        TeamFact.GOOD_TYPE_COVERAGE -> Pair(
            "Good type coverage",
            "Your team doesn't have any major weaknesses"
        )
    }
}