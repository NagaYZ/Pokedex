package fr.uge.pokedex.data.user

import androidx.room.*

@Entity
class Profile(
    @ColumnInfo(name = "profile_name")
    private var profileName: String
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

    fun getProfileName(): String{
        return this.profileName
    }

    fun setProfileName(profileName:String){
        this.profileName = profileName
    }

    override fun toString(): String {
        return "Profile(profileName='$profileName')"
    }


}