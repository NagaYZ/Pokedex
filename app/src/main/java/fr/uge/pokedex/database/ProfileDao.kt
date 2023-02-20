package fr.uge.pokedex.database

import androidx.room.*
import fr.uge.pokedex.components.Route

@Dao
interface ProfileDao {

    @Insert
    fun addProfile(profile: Profile): Long

    @Delete
    fun deleteProfile(profile: Profile)

    @Update
    fun updateProfile(profile: Profile)

    @Query("SELECT * FROM profile")
    fun getAllProfiles() :List<Profile>

    @Query("DELETE FROM profile")
    fun deleteAllProfiles()

    @Transaction
    @Query("SELECT * FROM profile")
    fun getAllProfilesWithFavorites(): List<ProfileWithFavorites>

    @Transaction
    @Query("SELECT * FROM profile WHERE id = :profileId")
    fun getProfileWithFavorites(profileId :Long): ProfileWithFavorites

    @Query("SELECT * FROM profile WHERE id = :profileId")
    fun getProfile(profileId :Long): Profile

    @Transaction
    @Query("SELECT * FROM profile WHERE id = :profileId")
    fun getProfileWithTeam(profileId :Long): ProfileWithTeam

}