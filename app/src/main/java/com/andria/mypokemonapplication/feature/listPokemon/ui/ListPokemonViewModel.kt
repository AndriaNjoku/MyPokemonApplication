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
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ListPokemonViewModel @Inject constructor(
    private val repository: PokemonRepository,
): ViewModel() {
    private val _state: MutableStateFlow<ListPokemonState> = MutableStateFlow(ListPokemonState.Loading)
    val state: StateFlow<ListPokemonState> = _state.asStateFlow()

    private val _currentPage = MutableStateFlow(0)
    val currentPage: StateFlow<Int> = _currentPage
    private val pageSize = 20


    init {
        fetchCurrentPage()
    }

    //TODO: only supports sequential pagination for simplicity but could be improved
    private fun fetchCurrentPage() {
        viewModelScope.launch {
            _state.value = ListPokemonState.Loading
            val offset = _currentPage.value * pageSize
            when (val res = repository.getPokemonList(offset)) {
                is RepoResult.Success<*> ->
                    handleSuccessList(res as RepoResult.Success<List<PokemonLite>>)
                is RepoResult.Failure   ->
                    handleFailure()
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

    fun nextPage() {
        _currentPage.update { it + 1 }
        fetchCurrentPage()
    }

    fun previousPage() {
        _currentPage.update { (it - 1).coerceAtLeast(0) }
        fetchCurrentPage()
    }


    suspend fun backToList() {
        val offset = _currentPage.value * pageSize
        when (val cached = repository.getPokemonList(offset)) {
            is RepoResult.Success<*> -> handleSuccessList(cached as RepoResult.Success<List<PokemonLite>>)
            is RepoResult.Failure   -> handleFailure()
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
