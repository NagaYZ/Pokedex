package fr.uge.pokedex.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Profile::class, Favorite::class], version = 3)
abstract class PokedexAppDatabase : RoomDatabase() {
    abstract fun profileDao(): ProfileDao
    abstract fun favoriteDao(): FavoriteDao
}

object PokedexAppDatabaseConnection{

    lateinit var connection: PokedexAppDatabase

    fun initialise(context: Context){
        connection = Room.databaseBuilder(context, PokedexAppDatabase::class.java, "PokedexAppDatabase.db").allowMainThreadQueries().fallbackToDestructiveMigration().build()
    }

}