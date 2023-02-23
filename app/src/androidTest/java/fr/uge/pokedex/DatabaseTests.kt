package fr.uge.pokedex

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import fr.uge.pokedex.database.*
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.Assert.*

@RunWith(AndroidJUnit4::class)
class DatabaseTests {

    @Test
    fun addProfile(){
        PokedexAppDatabaseConnection.initialise(InstrumentationRegistry.getInstrumentation().targetContext)
        val profileDao : ProfileDao = PokedexAppDatabaseConnection.connection.profileDao()
        profileDao.deleteAllProfiles()

        profileDao.addProfile(Profile("Mat"))
        assertEquals(1, profileDao.getAllProfiles().size)
    }

    @Test
    fun deleteProfile(){
        PokedexAppDatabaseConnection.initialise(InstrumentationRegistry.getInstrumentation().targetContext)
        val profileDao : ProfileDao = PokedexAppDatabaseConnection.connection.profileDao()
        profileDao.deleteAllProfiles()

        val profileMatId:Long = profileDao.addProfile(Profile("Mat"))
        profileDao.deleteProfile(profileDao.getProfile(profileMatId))
        assertEquals(0, profileDao.getAllProfiles().size)
    }

    @Test
    fun updateProfile(){
        PokedexAppDatabaseConnection.initialise(InstrumentationRegistry.getInstrumentation().targetContext)
        val profileDao : ProfileDao = PokedexAppDatabaseConnection.connection.profileDao()
        profileDao.deleteAllProfiles()

        val profileMatId:Long = profileDao.addProfile(Profile("Mat"))

        val profileMat = profileDao.getProfile(profileMatId)
        profileMat.setProfileName("Jean")
        profileDao.updateProfile(profileMat)

        assertEquals("Jean", profileDao.getProfile(profileMat.getId()).getProfileName())
    }

    @Test
    fun addFavoriteToProfile(){
        PokedexAppDatabaseConnection.initialise(InstrumentationRegistry.getInstrumentation().targetContext)
        val profileDao : ProfileDao = PokedexAppDatabaseConnection.connection.profileDao()
        val favoriteDao : FavoriteDao = PokedexAppDatabaseConnection.connection.favoriteDao()
        profileDao.deleteAllProfiles()

        val profileMatId:Long = profileDao.addProfile(Profile("Mat"))
        favoriteDao.addFavorite(Favorite(2, profileMatId))
        favoriteDao.addFavorite(Favorite(50, profileMatId))
        favoriteDao.addFavorite(Favorite(100, profileMatId))
        favoriteDao.addFavorite(Favorite(5, profileMatId))
        favoriteDao.addFavorite(Favorite(42, profileMatId))
        assertEquals(5, profileDao.getProfileWithFavorites(profileMatId).favorites.size)
    }

    @Test
    fun removeFavoriteFromProfile(){
        PokedexAppDatabaseConnection.initialise(InstrumentationRegistry.getInstrumentation().targetContext)
        val profileDao : ProfileDao = PokedexAppDatabaseConnection.connection.profileDao()
        val favoriteDao : FavoriteDao = PokedexAppDatabaseConnection.connection.favoriteDao()
        profileDao.deleteAllProfiles()

        val profileMatId:Long = profileDao.addProfile(Profile("Mat"))
        favoriteDao.addFavorite(Favorite(2, profileMatId))
        favoriteDao.addFavorite(Favorite(50, profileMatId))
        favoriteDao.addFavorite(Favorite(100, profileMatId))
        favoriteDao.addFavorite(Favorite(5, profileMatId))
        favoriteDao.addFavorite(Favorite(42, profileMatId))

        val profileWithFavorites = profileDao.getProfileWithFavorites(profileMatId)
        favoriteDao.deleteFavorite(profileWithFavorites.favorites.first())

        assertEquals(4, profileDao.getProfileWithFavorites(profileMatId).favorites.size)
    }

    @Test
    fun addTeam(){
        PokedexAppDatabaseConnection.initialise(InstrumentationRegistry.getInstrumentation().targetContext)
        val teamDao : TeamDao = PokedexAppDatabaseConnection.connection.teamDao()
        val profileDao : ProfileDao = PokedexAppDatabaseConnection.connection.profileDao()
        profileDao.deleteAllProfiles()

        val profileMatId:Long = profileDao.addProfile(Profile("Mat"))
        val teamId:Long  = teamDao.addTeam(Team("Team de Mat", profileMatId))

        assertEquals("Team de Mat", teamDao.getTeam(teamId).getTeamName())
    }

    @Test
    fun addMembersToTeam(){
        PokedexAppDatabaseConnection.initialise(InstrumentationRegistry.getInstrumentation().targetContext)
        val teamDao : TeamDao = PokedexAppDatabaseConnection.connection.teamDao()
        val profileDao : ProfileDao = PokedexAppDatabaseConnection.connection.profileDao()
        val teamMemberDao : TeamMemberDao = PokedexAppDatabaseConnection.connection.teamMemberDao()
        profileDao.deleteAllProfiles()

        val profileMatId:Long = profileDao.addProfile(Profile("Mat"))
        val teamId:Long  = teamDao.addTeam(Team("Team de Mat", profileMatId))
        teamMemberDao.addTeamMember(TeamMember(1, teamId))
        teamMemberDao.addTeamMember(TeamMember(2, teamId))
        teamMemberDao.addTeamMember(TeamMember(3, teamId))
        teamMemberDao.addTeamMember(TeamMember(4, teamId))
        teamMemberDao.addTeamMember(TeamMember(5, teamId))
        teamMemberDao.addTeamMember(TeamMember(6, teamId))

        assertEquals(6, teamDao.getTeamWithMembers(teamId).teamMembers.size)
    }

    @Test
    fun removeMembersFromTeam(){
        PokedexAppDatabaseConnection.initialise(InstrumentationRegistry.getInstrumentation().targetContext)
        val teamDao : TeamDao = PokedexAppDatabaseConnection.connection.teamDao()
        val profileDao : ProfileDao = PokedexAppDatabaseConnection.connection.profileDao()
        val teamMemberDao : TeamMemberDao = PokedexAppDatabaseConnection.connection.teamMemberDao()
        profileDao.deleteAllProfiles()

        val profileMatId:Long = profileDao.addProfile(Profile("Mat"))
        val teamId:Long  = teamDao.addTeam(Team("Team de Mat", profileMatId))
        teamMemberDao.addTeamMember(TeamMember(1, teamId))
        teamMemberDao.addTeamMember(TeamMember(2, teamId))
        teamMemberDao.addTeamMember(TeamMember(3, teamId))
        teamMemberDao.addTeamMember(TeamMember(4, teamId))
        teamMemberDao.addTeamMember(TeamMember(5, teamId))
        teamMemberDao.addTeamMember(TeamMember(6, teamId))

        val teamWithMembers = teamDao.getTeamWithMembers(teamId)
        teamMemberDao.deleteTeamMember(teamWithMembers.teamMembers[0])
        teamMemberDao.deleteTeamMember(teamWithMembers.teamMembers[1])

        assertEquals("[TeamMember(pokemonId=3), TeamMember(pokemonId=4), TeamMember(pokemonId=5), TeamMember(pokemonId=6)]", teamDao.getTeamWithMembers(teamId).teamMembers.toString())
    }

    @Test
    fun deleteTeamWithMembers(){
        PokedexAppDatabaseConnection.initialise(InstrumentationRegistry.getInstrumentation().targetContext)
        val teamDao : TeamDao = PokedexAppDatabaseConnection.connection.teamDao()
        val profileDao : ProfileDao = PokedexAppDatabaseConnection.connection.profileDao()
        val teamMemberDao : TeamMemberDao = PokedexAppDatabaseConnection.connection.teamMemberDao()
        profileDao.deleteAllProfiles()

        val profileMatId:Long = profileDao.addProfile(Profile("Mat"))
        val teamId:Long  = teamDao.addTeam(Team("Team de Mat", profileMatId))
        val firstMemberId = teamMemberDao.addTeamMember(TeamMember(1, teamId))
        teamMemberDao.addTeamMember(TeamMember(2, teamId))
        teamMemberDao.addTeamMember(TeamMember(3, teamId))
        teamMemberDao.addTeamMember(TeamMember(4, teamId))
        teamMemberDao.addTeamMember(TeamMember(5, teamId))
        teamMemberDao.addTeamMember(TeamMember(6, teamId))

        val team:Team = teamDao.getTeam(teamId)
        teamDao.deleteTeam(team)

        assertEquals(null, teamDao.getTeam(teamId))
        assertEquals(null, teamMemberDao.getTeamMember(firstMemberId))
    }

    @Test
    fun getProfileWithTeam(){
        PokedexAppDatabaseConnection.initialise(InstrumentationRegistry.getInstrumentation().targetContext)
        val teamDao : TeamDao = PokedexAppDatabaseConnection.connection.teamDao()
        val profileDao : ProfileDao = PokedexAppDatabaseConnection.connection.profileDao()
        val teamMemberDao : TeamMemberDao = PokedexAppDatabaseConnection.connection.teamMemberDao()
        profileDao.deleteAllProfiles()

        val profileMatId:Long = profileDao.addProfile(Profile("Mat"))
        val teamId:Long  = teamDao.addTeam(Team("Team de Mat", profileMatId))
        teamMemberDao.addTeamMember(TeamMember(1, teamId))
        teamMemberDao.addTeamMember(TeamMember(2, teamId))
        teamMemberDao.addTeamMember(TeamMember(3, teamId))
        teamMemberDao.addTeamMember(TeamMember(4, teamId))
        teamMemberDao.addTeamMember(TeamMember(5, teamId))
        teamMemberDao.addTeamMember(TeamMember(6, teamId))

        assertEquals(6, profileDao.getProfileWithTeam(profileMatId).teamsWithMembers.first().teamMembers.size)
    }


}