package com.andria.mypokemonapplication.data.pokemon.api

import com.andria.mypokemonapplication.data.pokemon.api.model.PokemonFullDto
import com.andria.mypokemonapplication.data.pokemon.api.model.PokemonListResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query



interface PokemonApi {
    /**
     * GET https://pokeapi.co/api/v2/pokemon?offset={offset}&limit={limit}
     * Returns a paged list of Pokémon.
     */
    @GET("pokemon")
    suspend fun getPokemonPage(
        @Query("offset") offset: Int,
        @Query("limit") limit: Int = 20
    ): Response<PokemonListResponse>

    @GET("pokemon/{idOrName}")
    suspend fun getPokemon(@Path("idOrName") idOrName: String): Response<PokemonFullDto>


}
