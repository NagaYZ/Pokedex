package fr.uge.pokedex.data.user

import androidx.room.*

@Dao
interface FavoriteDao {

    @Insert
    fun addFavorite(favorite: Favorite) : Long

    @Delete
    fun deleteFavorite(favorite: Favorite)
}