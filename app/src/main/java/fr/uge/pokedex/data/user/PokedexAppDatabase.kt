package fr.uge.pokedex.data.user

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Profile::class, Favorite::class, Team::class, TeamMember::class], version = 7)
abstract class PokedexAppDatabase : RoomDatabase() {
    abstract fun profileDao(): ProfileDao
    abstract fun favoriteDao(): FavoriteDao
    abstract fun teamDao(): TeamDao
    abstract fun teamMemberDao(): TeamMemberDao

    companion object {

        @Volatile
        private var connection: PokedexAppDatabase? = null

        fun getConnection(context: Context): PokedexAppDatabase {
            synchronized(this) {
                if (this.connection == null) {
                    this.connection = Room.databaseBuilder(context, PokedexAppDatabase::class.java, "PokedexAppDatabase.db").fallbackToDestructiveMigration().build()
                    return this.connection!!
                } else {
                    return this.connection!!
                }
            }
        }
    }
}

