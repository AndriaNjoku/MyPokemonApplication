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
    // Handy: derive numeric ID from the URL if you need it
    val id: Int get() = url.trimEnd('/').substringAfterLast('/').toIntOrNull() ?: -1
}