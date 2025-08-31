package com.andria.mypokemonapplication.data.pokemon.api

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

data class PokemonListResponse(
    val count: Int,
    val next: String?,
    val previous: String?,
    val results: List<PokemonItem>
)

data class PokemonItem(
    val name: String,
    val url: String
) {
    // Handy: derive numeric ID from the URL if you need it
    val id: Int get() = url.trimEnd('/').substringAfterLast('/').toIntOrNull() ?: -1
}

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
}
