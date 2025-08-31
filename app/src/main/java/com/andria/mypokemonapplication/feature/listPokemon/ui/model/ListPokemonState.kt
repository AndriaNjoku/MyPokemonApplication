package com.andria.mypokemonapplication.feature.listPokemon.ui.model

import com.andria.mypokemonapplication.domain.pokemon.model.Pokemon

sealed class ListPokemonState{
    object Loading: ListPokemonState()
    data class Success(val pokemonList: List<Pokemon>): ListPokemonState()

    object RetryError: ListPokemonState()
}
