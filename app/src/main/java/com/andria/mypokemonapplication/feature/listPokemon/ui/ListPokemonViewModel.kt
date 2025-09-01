package com.andria.mypokemonapplication.feature.listPokemon.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.andria.mypokemonapplication.domain.pokemon.model.PokemonFull
import com.andria.mypokemonapplication.domain.pokemon.model.PokemonLite
import com.andria.mypokemonapplication.domain.pokemon.repository.PokemonRepository
import com.andria.mypokemonapplication.domain.pokemon.repository.RepoResult
import com.andria.mypokemonapplication.feature.listPokemon.ui.model.ListPokemonState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ListPokemonViewModel @Inject constructor(
    private val repository: PokemonRepository,
): ViewModel() {
    var pagesFetched = 0
    private val _state: MutableStateFlow<ListPokemonState> = MutableStateFlow(ListPokemonState.Loading)
    val state: StateFlow<ListPokemonState> = _state.asStateFlow()

    init {
        fetchPokemon()
    }

    //TODO: only supports sequential pagination for simplicity but could be improved
    fun fetchPokemon(){
        pagesFetched ++

        viewModelScope.launch {
            _state.value = ListPokemonState.Loading

            val response: RepoResult<List<PokemonLite>> = repository.getPokemonList(offset = 20 * pagesFetched)

            when(response){
                is RepoResult.Failure ->{
                    handleFailure()
                }
                is RepoResult.Success<*> -> {
                    handleSuccessList(response as RepoResult.Success<List<PokemonLite>>)
                }
            }
        }
    }

    fun fetchPokemonDetails(pokemonId: Int){
        viewModelScope.launch {
            _state.value = ListPokemonState.Loading

            val response: RepoResult<PokemonFull> = repository.getPokemonInfo(pokemonId)

            when(response){
                is RepoResult.Failure ->{
                    handleFailure()
                }
                is RepoResult.Success<*> -> {
                    handleSuccessDetails(response as RepoResult.Success<PokemonFull>)
                }
            }

        }
    }

    suspend fun backToList() {
        val cachedList = repository.getPokemonList(pagesFetched * 20)

        when(cachedList){
            is RepoResult.Failure ->{
                handleFailure()
            }
            is RepoResult.Success<*> -> {
                handleSuccessList(cachedList as RepoResult.Success<List<PokemonLite>>)
            }
        }
    }

    private fun handleSuccessList(response: RepoResult.Success<List<PokemonLite>>) {
        _state.value = ListPokemonState.SuccessList(response.data)
    }

    private fun handleSuccessDetails(response: RepoResult.Success<PokemonFull>) {
        _state.value = ListPokemonState.SuccessDetails(response.data)
    }

    private fun handleFailure() {
        _state.value = ListPokemonState.RetryError
    }
}
