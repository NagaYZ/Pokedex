package fr.uge.pokedex.database

import androidx.room.*

@Entity(foreignKeys = [ForeignKey(entity = Profile::class, parentColumns = arrayOf("id"), childColumns = arrayOf("profile_id"), onDelete = ForeignKey.CASCADE)], indices = [Index(value = ["profile_id"])])
class Team(
    @ColumnInfo(name = "team_name")
    private var teamName: String,

    @ColumnInfo(name = "profile_id")
    private var profileId: Long,
){

    @ColumnInfo(name = "team_id")
    @PrimaryKey(autoGenerate = true)
    private var teamId: Long = 0

    fun getTeamId(): Long {
        return this.teamId
    }

    fun setTeamId(teamId :Long) {
        this.teamId = teamId
    }

    fun getTeamName(): String {
        return this.teamName
    }

    fun setTeamName(teamName :String) {
        this.teamName = teamName
    }

    fun getProfileId(): Long {
        return this.profileId
    }

    fun setProfileId(profileId :Long) {
        this.profileId = profileId
    }

    override fun toString(): String {
        return "Team(teamName='$teamName')"
    }


}
