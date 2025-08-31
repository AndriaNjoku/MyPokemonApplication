package com.andria.mypokemonapplication.domain.pokemon.repository

import com.andria.mypokemonapplication.domain.pokemon.model.Pokemon

interface PokemonRepository {
    suspend fun getPokemonList( offset: Int): RepoResult<List<Pokemon>>

    suspend fun getPokemonInfo(pokemonId: Int):RepoResult<Pokemon>
}