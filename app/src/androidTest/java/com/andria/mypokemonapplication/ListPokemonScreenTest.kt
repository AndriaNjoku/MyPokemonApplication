package com.andria.mypokemonapplication

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.andria.mypokemonapplication.domain.pokemon.model.PokemonLite
import com.andria.mypokemonapplication.feature.listPokemon.ui.ListPokemonContent
import org.junit.Rule
import org.junit.Test

class ListPokemonScreenTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    @Test
    fun pokemonList_displaysNames_andPaginationWorks() {
        val pokemonList = listOf(
            PokemonLite(1, "Bulbasaur", imageUrl = ""),
            PokemonLite(2, "Ivysaur", imageUrl = ""),
            PokemonLite(3, "Venusaur", imageUrl = "")
        )
        var nextClicked = false

        composeTestRule.setContent {
            ListPokemonContent(
                modifier = androidx.compose.ui.Modifier,
                pokemonList = pokemonList,
                currentPage = 1,
                onClick = {},
                onNextPage = { nextClicked = true },
                onPreviousPage = {}
            )
        }

        composeTestRule.onNodeWithText("Bulbasaur").assertIsDisplayed()
        composeTestRule.onNodeWithText("Ivysaur").assertIsDisplayed()

        composeTestRule.onNodeWithContentDescription("Next page").performClick()


        assert(nextClicked) { "Next button was not clicked" }
    }
}
