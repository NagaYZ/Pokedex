package fr.uge.pokedex.data.user

import androidx.room.*

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
    @Query("SELECT * FROM profile WHERE id = :profileId")
    fun getProfileWithFavorites(profileId :Long): ProfileWithFavorites

    @Query("SELECT * FROM profile WHERE id = :profileId")
    fun getProfile(profileId :Long): Profile

    @Transaction
    @Query("SELECT * FROM profile WHERE id = :profileId")
    fun getProfileWithTeam(profileId :Long): ProfileWithTeam

}