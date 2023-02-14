package fr.uge.pokedex.database

object ProfilesService {

    private var currentProfile: Profile? = null

    fun setCurrentProfile(profile: Profile){
        currentProfile = profile
    }

    fun getCurrentProfile(): Profile? {
        return currentProfile
    }
}