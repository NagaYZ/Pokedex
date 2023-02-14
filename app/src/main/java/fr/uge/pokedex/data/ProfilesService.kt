package fr.uge.pokedex.data

object ProfilesService {

    private val profilesMap :HashMap<String,Profile> = HashMap()
    private var currentProfile: Profile? = null

    fun addProfile(profile: Profile){
        this.profilesMap.put(profile.getProfileName(), profile)
    }

    fun deleteProfile(profile: Profile){
        profilesMap.remove(profile.getProfileName())
    }

    fun getProfiles() :MutableList<Profile>{
        return profilesMap.values.toMutableList()
    }

    fun editProfile(profile: Profile, newProfileName:String){
        profilesMap.remove(profile.getProfileName())
        profile.setProfileName(newProfileName)
        profilesMap.put(profile.getProfileName(), profile)
    }
    fun setCurrentProfile(profile: Profile){
        this.currentProfile = profile
    }

    fun getCurrentProfile(): Profile? {
        return this.currentProfile
    }
}