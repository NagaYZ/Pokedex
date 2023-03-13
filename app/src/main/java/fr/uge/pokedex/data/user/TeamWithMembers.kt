package fr.uge.pokedex.data.user

import androidx.room.Embedded
import androidx.room.Relation

data class TeamWithMembers(
    @Embedded var team: Team,

    @Relation(parentColumn = "team_id", entityColumn = "team_id")
    val teamMembers : List<TeamMember>
)