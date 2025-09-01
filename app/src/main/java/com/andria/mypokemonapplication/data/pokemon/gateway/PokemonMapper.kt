package com.andria.mypokemonapplication.data.pokemon.gateway

import com.andria.mypokemonapplication.data.pokemon.api.model.PokemonFullDto
import com.andria.mypokemonapplication.data.pokemon.api.model.PokemonListResponse
import com.andria.mypokemonapplication.domain.pokemon.model.PokemonFull
import com.andria.mypokemonapplication.domain.pokemon.model.PokemonLite

object PokemonMapper {
    fun map(response: PokemonListResponse): List<PokemonLite> {
        return response.results.map { pokemonItem ->
            PokemonLite(
                id = pokemonItem.id,
                name = pokemonItem.name,
                imageUrl = pokemonItem.url,
            )
        }
    }

    fun map(pokemonDto: PokemonFullDto): PokemonFull {
        return PokemonFull(
            id = pokemonDto.id,
            name = pokemonDto.name,
            imageUrl = pokemonDto.sprites.frontDefault ?: "",
            heightMeters = pokemonDto.height / 10.0
        )
    }


}
