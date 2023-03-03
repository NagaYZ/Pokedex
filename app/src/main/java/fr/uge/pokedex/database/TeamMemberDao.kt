package fr.uge.pokedex.database

import androidx.room.*

@Dao
interface TeamMemberDao {

    @Insert
    fun addTeamMember(teamMember: TeamMember): Long

    @Delete
    fun deleteTeamMember(teamMember: TeamMember)

    @Query("SELECT * FROM teammember WHERE team_member_id = :teamMemberId")
    fun getTeamMember(teamMemberId :Long): TeamMember
}