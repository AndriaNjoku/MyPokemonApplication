package com.andria.mypokemonapplication.feature.listPokemon.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.andria.mypokemonapplication.domain.pokemon.model.PokemonFull

@Composable
fun PokemonDetailsComponent(
    pokemon: PokemonFull,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Name
        Text(
            text = pokemon.name,
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )

        Spacer(Modifier.height(24.dp))

        // Image
        AsyncImage(
            model = pokemon.imageUrl,
            contentDescription = "${pokemon.name} image",
            modifier = Modifier.size(200.dp)
        )

        Spacer(Modifier.height(24.dp))

        // Height
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Height:",
                style = MaterialTheme.typography.titleMedium
            )
            Spacer(Modifier.width(8.dp))
            Text(
                text = String.format("%.1f m", pokemon.heightMeters),
                style = MaterialTheme.typography.titleLarge
            )
        }
    }
}
