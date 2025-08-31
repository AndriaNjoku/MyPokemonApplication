package com.andria.mypokemonapplication.feature.listPokemon.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.andria.mypokemonapplication.domain.pokemon.model.Pokemon
import com.andria.mypokemonapplication.feature.listPokemon.ui.model.ListPokemonState

@Composable
fun ListPokemonScreen(
    modifier: Modifier = Modifier,
    viewModel: ListPokemonViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsState()

    when (state) {
        ListPokemonState.Loading -> {

        }

        ListPokemonState.RetryError -> {

        }

        is ListPokemonState.Success -> {
            ListPokemonComponent(
                modifier,
                (state as ListPokemonState.Success).pokemonList,
            )
        }
    }
}

@Composable
fun ListPokemonComponent(
    modifier: Modifier,
    pokemonList: List<Pokemon>
) {
    Surface(modifier = modifier) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            LazyColumn(
            ) {
                items(pokemonList) { pokemon ->
                    Text(text = pokemon.name)
                }
            }
        }
    }
}
