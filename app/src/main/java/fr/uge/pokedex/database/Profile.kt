package fr.uge.pokedex.database

import androidx.room.*

@Entity
class Profile(
    @ColumnInfo(name = "first_name")
    var profileName: String
) {

    @ColumnInfo(name = "id")
    @PrimaryKey(autoGenerate = true)
    private var id: Long = 0

    fun getId(): Long {
        return this.id
    }

    fun setId(id :Long) {
        this.id = id
    }

    override fun toString(): String {
        return "Profile(profileName='$profileName')"
    }


}