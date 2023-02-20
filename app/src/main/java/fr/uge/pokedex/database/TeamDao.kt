package fr.uge.pokedex.database

import androidx.room.*

@Dao
interface TeamDao {

    @Insert
    fun addTeam(team: Team): Long

    @Delete
    fun deleteTeam(team: Team)

    @Update
    fun updateTeam(team: Team)

    @Query("SELECT * FROM team WHERE team_id = :teamId")
    fun getTeam(teamId :Long): Team

    @Transaction
    @Query("SELECT * FROM team WHERE team_id = :teamId")
    fun getTeamWithMembers(teamId :Long): TeamWithMembers
}