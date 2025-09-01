package com.andria.mypokemonapplication.data.pokemon.api.model

data class PokemonListResponse(
    val count: Int,
    val next: String?,
    val previous: String?,
    val results: List<PokemonLightDto>
)

data class PokemonLightDto(
    val name: String,
    val url: String
) {
    val id: Int get() = url.trimEnd('/').substringAfterLast('/').toIntOrNull() ?: -1
}