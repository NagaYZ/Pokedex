package fr.uge.pokedex.database

import androidx.room.*

@Dao
interface FavoriteDao {

    @Insert
    fun addFavorite(favorite: Favorite) : Long

    @Delete
    fun deleteFavorite(favorite: Favorite)
}