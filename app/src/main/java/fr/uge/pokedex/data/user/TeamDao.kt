package fr.uge.pokedex.data.user

import androidx.room.*

@Dao
interface TeamDao {

    @Insert
    suspend fun addTeam(team: Team): Long

    @Delete
    suspend fun deleteTeam(team: Team)

    @Update
    suspend fun updateTeam(team: Team)

    @Query("SELECT * FROM team WHERE team_id = :teamId")
    suspend fun getTeam(teamId :Long): Team

    @Transaction
    @Query("SELECT * FROM team WHERE team_id = :teamId")
    suspend fun getTeamWithMembers(teamId :Long): TeamWithMembers
}