package com.andria.mypokemonapplication.domain.pokemon.repository

import com.andria.mypokemonapplication.domain.pokemon.model.PokemonFull
import com.andria.mypokemonapplication.domain.pokemon.model.PokemonLite

interface PokemonRepository {
    suspend fun getPokemonList( offset: Int): RepoResult<List<PokemonLite>>

    suspend fun getPokemonInfo(pokemonId: Int):RepoResult<PokemonFull>
}