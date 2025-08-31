package com.andria.mypokemonapplication.domain.pokemon.model

data class Pokemon(
    val id: Int,
    val name: String,
    val type: String? = null,
    val imageUrl: String
    )
