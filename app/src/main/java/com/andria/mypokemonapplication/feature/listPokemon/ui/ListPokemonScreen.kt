package com.andria.mypokemonapplication.feature.listPokemon.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.andria.mypokemonapplication.StandardTopAppBar
import com.andria.mypokemonapplication.domain.pokemon.model.PokemonFull
import com.andria.mypokemonapplication.domain.pokemon.model.PokemonLite
import com.andria.mypokemonapplication.feature.listPokemon.components.PokemonDetailsComponent
import com.andria.mypokemonapplication.feature.listPokemon.components.PokemonNameCard
import com.andria.mypokemonapplication.feature.listPokemon.ui.model.ListPokemonState
import kotlinx.coroutines.launch

@Composable
fun ListPokemonScreen(
    modifier: Modifier = Modifier,
    viewModel: ListPokemonViewModel = hiltViewModel(),
) {

    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            StandardTopAppBar(
                screenName = "Pokemon List"
            ) {
                coroutineScope.launch {
                    viewModel.backToList()
                }
            }
        },
        modifier = Modifier.fillMaxSize()
    ) { innerPadding ->


        val state by viewModel.state.collectAsState()

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            when (state) {
                ListPokemonState.Loading -> {

                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "Loading...",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                }

                ListPokemonState.RetryError -> {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "Error loading data",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Please try again",
                            style = MaterialTheme.typography.bodyMedium
                        )
                        Spacer(modifier = Modifier.height(16.dp))

                    }
                }

                is ListPokemonState.SuccessList -> {
                    ListPokemonContent(
                        modifier,
                        (state as ListPokemonState.SuccessList).pokemonList,
                    ) {
                        viewModel.fetchPokemonDetails(it)
                    }
                }

                is ListPokemonState.SuccessDetails -> {
                    PokemonDetailsContent(
                        modifier,
                        (state as ListPokemonState.SuccessDetails).pokemon,
                    )
                }
            }
        }
    }
}

@Composable
fun PokemonDetailsContent(
    modifier: Modifier,
    pokemon: PokemonFull
) {
    PokemonDetailsComponent(
        pokemon = pokemon,
        modifier = modifier
    )
}


@Composable
fun ListPokemonContent(
    modifier: Modifier,
    pokemonList: List<PokemonLite>,
    onClick: (Int) -> Unit,
) {
    Surface(
        modifier = modifier,
        color = Color.LightGray
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            LazyColumn(
            ) {
                items(pokemonList) { pokemon ->
                    PokemonNameCard(
                        name = pokemon.name,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        onClick = {
                            onClick(pokemon.id)
                        }
                    )
                }
            }
        }
    }
}
