package fr.uge.pokedex.database

import androidx.room.Embedded
import androidx.room.Relation

data class ProfileWithFavorites(
    @Embedded var profile: Profile,

    @Relation(parentColumn = "id", entityColumn = "profile_id")
    val favorites : List<Favorite>
)