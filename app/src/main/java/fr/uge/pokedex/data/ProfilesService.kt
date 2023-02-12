package fr.uge.pokedex.data

object ProfilesService {

    private val profilesMap :HashMap<String,Profile> = HashMap()

    fun addProfile(profile: Profile) :Unit{
        this.profilesMap.put(profile.getProfileName(), profile)
    }

    fun deleteProfile(profile: Profile) :Unit{
        profilesMap.remove(profile.getProfileName())
    }

    fun getProfiles() :MutableList<Profile>{
        return profilesMap.values.toMutableList();
    }

    fun editProfile(profile: Profile, newProfileName:String){
        profilesMap.remove(profile.getProfileName())
        profile.setProfileName(newProfileName)
        profilesMap.put(profile.getProfileName(), profile)
    }
}