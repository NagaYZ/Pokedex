package fr.uge.pokedex.database

import androidx.room.*

@Entity(foreignKeys = [ForeignKey(entity = Team::class, parentColumns = arrayOf("team_id"), childColumns = arrayOf("team_id"), onDelete = ForeignKey.CASCADE)], indices = [Index(value = ["team_id"])])
class TeamMember(
    @ColumnInfo(name = "pokemon_id")
    private var pokemonId: Long,

     @ColumnInfo(name = "team_id")
     private var teamId: Long
) {

    @ColumnInfo(name = "team_member_id")
    @PrimaryKey(autoGenerate = true)
    private var teamMemberId: Long = 0

    fun getTeamMemberId(): Long {
        return this.teamMemberId
    }

    fun setTeamMemberId(teamMemberId :Long) {
        this.teamMemberId = teamMemberId
    }

    fun getPokemonId(): Long{
        return this.pokemonId
    }

    fun setPokemonId(pokemonId: Long){
        this.pokemonId = pokemonId
    }

    fun getTeamId(): Long{
        return this.teamId
    }

    fun setTeamId(teamId: Long){
        this.teamId = teamId
    }

    override fun toString(): String {
        return "TeamMember(pokemonId=$pokemonId)"
    }


}