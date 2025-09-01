package com.andria.mypokemonapplication.feature.listPokemon.ui.model

import com.andria.mypokemonapplication.domain.pokemon.model.PokemonFull
import com.andria.mypokemonapplication.domain.pokemon.model.PokemonLite

sealed class ListPokemonState{
    object Loading: ListPokemonState()

    data class SuccessList(val pokemonList: List<PokemonLite>): ListPokemonState()

    data class SuccessDetails(val pokemon: PokemonFull): ListPokemonState()

    object RetryError: ListPokemonState()
}
