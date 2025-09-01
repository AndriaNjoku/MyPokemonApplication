package com.andria.mypokemonapplication.domain.pokemon.model

data class PokemonFull(
    val id: Int,
    val name: String,
    val heightMeters: Double, // in decimetres
    val imageUrl: String
    )
