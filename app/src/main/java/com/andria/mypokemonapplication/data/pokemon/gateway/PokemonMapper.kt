package com.andria.mypokemonapplication.data.pokemon.gateway

import com.andria.mypokemonapplication.data.pokemon.api.PokemonListResponse
import com.andria.mypokemonapplication.domain.pokemon.model.Pokemon

object PokemonMapper {
    fun map(response: PokemonListResponse): List<Pokemon> {
        return response.results.map { pokemonItem ->
            Pokemon(
                id = pokemonItem.id,
                name = pokemonItem.name,
                imageUrl = pokemonItem.url,
            )
        }
    }



}
