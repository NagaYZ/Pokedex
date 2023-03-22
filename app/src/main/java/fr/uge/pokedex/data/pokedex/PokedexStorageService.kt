package fr.uge.pokedex.data.pokedex

import android.content.Context
import fr.uge.pokedex.data.pokedex.ability.Ability
import fr.uge.pokedex.data.pokedex.ability.AbilityRepository
import fr.uge.pokedex.data.pokedex.evolution.EvolutionChain
import fr.uge.pokedex.data.pokedex.evolution.EvolutionChainRepository
import fr.uge.pokedex.data.pokedex.location.Location
import fr.uge.pokedex.data.pokedex.location.LocationRepository
import fr.uge.pokedex.data.pokedex.move.Move
import fr.uge.pokedex.data.pokedex.move.MoveRepository
import fr.uge.pokedex.data.pokedex.pokemon.Pokemon
import fr.uge.pokedex.data.pokedex.pokemon.PokemonRepository

object PokedexStorageService {
    private lateinit var pokemonRepository: PokemonRepository
    private lateinit var moveRepository: MoveRepository
    private lateinit var abilityRepository: AbilityRepository
    private lateinit var locationRepository: LocationRepository
    private lateinit var evolutionChainRepository: EvolutionChainRepository

    fun load(context: Context) {
        this.pokemonRepository = PokemonRepository(context)
        this.moveRepository = MoveRepository(context)
        this.abilityRepository = AbilityRepository(context)
        this.locationRepository = LocationRepository(context)
        this.evolutionChainRepository = EvolutionChainRepository(context)
    }

    fun getPokemon(id: Long): Pokemon? {
        return pokemonRepository.get(id)
    }

    fun getPokemonData(): Map<Long, Pokemon> {
        return pokemonRepository.data
    }

    fun getMove(id: Long): Move? {
        return moveRepository.get(id)
    }

    fun getAbility(id: Long): Ability? {
        return abilityRepository.get(id)
    }

    fun getEvolutionChain(id: Long): EvolutionChain? {
        return evolutionChainRepository.get(id)
    }

    fun getLocation(id: Long): Location? {
        return locationRepository.get(id)
    }
}