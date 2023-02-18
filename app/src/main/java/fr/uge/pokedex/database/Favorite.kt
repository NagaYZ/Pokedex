package fr.uge.pokedex.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.Companion.CASCADE
import androidx.room.PrimaryKey

@Entity(foreignKeys = [ForeignKey(entity = Profile::class, parentColumns = arrayOf("id"), childColumns = arrayOf("profile_id"), onDelete = CASCADE)])
class Favorite (
    @ColumnInfo(name = "pokemon_id")
    private var pokemonId: Long,

    @ColumnInfo(name = "profile_id")
    private var profileId:Long
){

    @ColumnInfo(name = "id")
    @PrimaryKey(autoGenerate = true)
    private var id: Long = 0

    fun getId(): Long {
        return this.id
    }

    fun setId(id :Long) {
        this.id = id
    }

    fun getProfileId(): Long {
        return this.profileId
    }

    fun setProfileId(id :Long) {
        this.profileId = profileId
    }

    fun getPokemonId(): Long {
        return this.pokemonId
    }

    fun setPokemonId(id :Long) {
        this.pokemonId = pokemonId
    }

    override fun toString(): String {
        return "Favorite(pokemonId=$pokemonId, profileId=$profileId, id=$id)"
    }


}