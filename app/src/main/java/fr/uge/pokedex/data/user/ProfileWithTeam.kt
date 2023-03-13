package fr.uge.pokedex.data.user

import androidx.room.Embedded
import androidx.room.Relation

data class ProfileWithTeam(
    @Embedded var profile: Profile,

    @Relation(entity = Team::class, parentColumn = "id", entityColumn = "profile_id")
    val teamsWithMembers : List<TeamWithMembers>
)