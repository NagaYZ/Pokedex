package fr.uge.pokedex.database

import androidx.room.*

@Dao
interface ProfileDao {

    @Insert
    fun addProfile(vararg profiles: Profile)

    @Delete
    fun deleteProfile(profile: Profile)

    @Update
    fun updateProfile(profile: Profile)

    @Query("SELECT * FROM profile")
    fun getProfiles() :List<Profile>
}