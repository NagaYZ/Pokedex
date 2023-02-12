package fr.uge.pokedex.data

class Profile {

    private var profileName : String

    constructor(profileName : String){
        this.profileName = profileName
    }

    fun getProfileName() :String{
        return profileName
    }

    fun setProfileName(newProfileName:String){
        this.profileName = newProfileName
    }

}