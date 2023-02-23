package fr.uge.pokedex.database

import androidx.room.*
import androidx.room.ForeignKey.Companion.CASCADE

@Entity(foreignKeys = [ForeignKey(entity = Profile::class, parentColumns = arrayOf("id"), childColumns = arrayOf("profile_id"), onDelete = CASCADE)], indices = [Index(value = ["profile_id"])])
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

    fun setProfileId(profileId :Long) {
        this.profileId = profileId
    }

    fun getPokemonId(): Long {
        return this.pokemonId
    }

    fun setPokemonId(pokemonId :Long) {
        this.pokemonId = pokemonId
    }

    override fun toString(): String {
        return "Favorite(pokemonId=$pokemonId)"
    }


}