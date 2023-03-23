package fr.uge.pokedex.data.user

import androidx.room.*

@Dao
interface ProfileDao {

    @Insert
    suspend fun addProfile(profile: Profile): Long

    @Delete
    suspend fun deleteProfile(profile: Profile)

    @Update
    suspend fun updateProfile(profile: Profile)

    @Query("SELECT * FROM profile")
    suspend fun getAllProfiles() :List<Profile>

    @Query("DELETE FROM profile")
    suspend fun deleteAllProfiles()

    @Transaction
    @Query("SELECT * FROM profile WHERE id = :profileId")
    suspend fun getProfileWithFavorites(profileId :Long): ProfileWithFavorites

    @Query("SELECT * FROM profile WHERE id = :profileId")
    suspend fun getProfile(profileId :Long): Profile

    @Transaction
    @Query("SELECT * FROM profile WHERE id = :profileId")
    suspend fun getProfileWithTeam(profileId :Long): ProfileWithTeam

}