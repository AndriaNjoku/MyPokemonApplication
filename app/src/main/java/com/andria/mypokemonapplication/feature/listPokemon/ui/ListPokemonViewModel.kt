package com.andria.mypokemonapplication.feature.listPokemon.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.andria.mypokemonapplication.domain.pokemon.model.Pokemon
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

    fun fetchPokemon(){
        pagesFetched ++

        viewModelScope.launch {
            _state.value = ListPokemonState.Loading

            val response: RepoResult<List<Pokemon>> = repository.getPokemonList(offset = 20 * pagesFetched)

            when(response){
                is RepoResult.Failure ->{
                    handleFailure()
                }
                is RepoResult.Success<*> -> {
                    handleSuccess(response as RepoResult.Success<List<Pokemon>>)
                }
            }
        }
    }

    private fun handleSuccess(response: RepoResult.Success<List<Pokemon>>) {
        _state.value = ListPokemonState.Success(response.data)
    }

    private fun handleFailure() {
        _state.value = ListPokemonState.RetryError
    }
}
