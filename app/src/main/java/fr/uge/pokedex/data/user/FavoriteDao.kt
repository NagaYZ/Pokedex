package fr.uge.pokedex.data.user

import androidx.room.*

@Dao
interface FavoriteDao {

    @Insert
    suspend fun addFavorite(favorite: Favorite) : Long

    @Delete
    suspend fun deleteFavorite(favorite: Favorite)
}