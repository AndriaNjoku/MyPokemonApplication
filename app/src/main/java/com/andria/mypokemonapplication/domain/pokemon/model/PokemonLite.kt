package com.andria.mypokemonapplication.domain.pokemon.model

data class PokemonLite(
    val id: Int,
    val name: String,
    val type: String? = null,
    val imageUrl: String
    )
