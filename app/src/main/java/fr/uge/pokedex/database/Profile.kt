package fr.uge.pokedex.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
class Profile(
    @ColumnInfo(name = "first_name")
    var profileName: String
) {

    @PrimaryKey(autoGenerate = true)
    private var id: Int = 0

    fun getId(): Int {
        return this.id
    }

    fun setId(id :Int) {
        this.id = id
    }
}