package fr.uge.pokedex.data.user

import androidx.room.*

@Dao
interface TeamMemberDao {

    @Insert
    suspend fun addTeamMember(teamMember: TeamMember): Long

    @Delete
    suspend fun deleteTeamMember(teamMember: TeamMember)

    @Query("SELECT * FROM teammember WHERE team_member_id = :teamMemberId")
    suspend fun getTeamMember(teamMemberId :Long): TeamMember
}