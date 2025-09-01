package com.andria.mypokemonapplication

import app.cash.turbine.test
import com.andria.mypokemonapplication.domain.pokemon.PokemonDomainError
import com.andria.mypokemonapplication.domain.pokemon.model.PokemonFull
import com.andria.mypokemonapplication.domain.pokemon.model.PokemonLite
import com.andria.mypokemonapplication.domain.pokemon.repository.PokemonRepository
import com.andria.mypokemonapplication.domain.pokemon.repository.RepoResult
import com.andria.mypokemonapplication.feature.listPokemon.ui.ListPokemonViewModel
import com.andria.mypokemonapplication.feature.listPokemon.ui.model.ListPokemonState
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.junit5.MockKExtension
import io.mockk.unmockkAll
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.extension.ExtendWith

@OptIn(ExperimentalCoroutinesApi::class)
class ListPokemonViewModelTest {

    private val dispatcher = StandardTestDispatcher()

    @MockK(relaxed = true)
    lateinit var repository: PokemonRepository

    @InjectMockKs
    lateinit var viewModel: ListPokemonViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(dispatcher)
        MockKAnnotations.init(this, relaxUnitFun = true)
        coEvery { repository.getPokemonList(any()) } returns RepoResult.Success(emptyList())
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        unmockkAll()
    }

    @Test
    fun `Given repo success When VM init Then emits Loading then SuccessList (offset 0)`() = runTest {
        val list = listOf(
            PokemonLite(1, "bulbasaur", imageUrl = ""),
            PokemonLite(2, "ivysaur", imageUrl = ""),
            )

        coEvery { repository.getPokemonList(0) } returns RepoResult.Success(list)

        // When
        viewModel = ListPokemonViewModel(repository)

        // Then
        viewModel.state.test {
            assertTrue(awaitItem() is ListPokemonState.Loading) // initial loading
            advanceUntilIdle()
            val success = awaitItem()
            assertTrue(success is ListPokemonState.SuccessList)
            assertEquals(list, (success as ListPokemonState.SuccessList).pokemonList)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `Given repo failure When VM init Then emits RetryError`() = runTest {
        // Given
        coEvery { repository.getPokemonList(any()) } returns RepoResult.Failure(PokemonDomainError.Unknown(
            Error("Something went wrong")))

        // When
        viewModel = ListPokemonViewModel(repository)

        // Then
        viewModel.state.test {
            assertTrue(awaitItem() is ListPokemonState.Loading)
            advanceUntilIdle()
            assertTrue(awaitItem() is ListPokemonState.RetryError)
        }
        coVerify { repository.getPokemonList(any()) }
    }

    @Test
    fun `Given page 0 loaded When nextPage Then calls offset 20 and emits SuccessList`() = runTest {
        // Given
        val page0 = RepoResult.Success((1..20).map { PokemonLite(it, "p$it", imageUrl = "") })
        val page1 = RepoResult.Success((21..40).map { PokemonLite(it, "p$it", imageUrl = "") })
        coEvery { repository.getPokemonList(0) } returns page0
        coEvery { repository.getPokemonList(20) } returns page1

        viewModel = ListPokemonViewModel(repository)

        viewModel.state.test {
            awaitItem()
            advanceUntilIdle()
            awaitItem()

            // When
            viewModel.nextPage()
            // Then
            assertTrue(awaitItem() is ListPokemonState.Loading)
            advanceUntilIdle()
            val s = awaitItem()
            assertTrue(s is ListPokemonState.SuccessList)
            assertEquals(20, (s as ListPokemonState.SuccessList).pokemonList.size)
        }
        coVerify { repository.getPokemonList(20) }
    }

    @Test
    fun `Given page 0 When previousPage Then clamps at 0 and reloads offset 0`() = runTest(dispatcher) {
        // Given
        coEvery { repository.getPokemonList(0) } returns RepoResult.Success(emptyList())
        viewModel = ListPokemonViewModel(repository)

        viewModel.state.test {
            awaitItem()
            advanceUntilIdle()
            awaitItem()

            // When
            viewModel.previousPage()

            // Then (still loads, but stays at 0)
            assertTrue(awaitItem() is ListPokemonState.Loading)
            advanceUntilIdle()
            assertTrue(awaitItem() is ListPokemonState.SuccessList)
        }
        coVerify(atLeast = 2) { repository.getPokemonList(0) }
    }

    @Test
    fun `Given current page 1 When backToList Then reloads same page offset 20`() = runTest(dispatcher) {
        // Given
        coEvery { repository.getPokemonList(0) } returns RepoResult.Success(emptyList())
        coEvery { repository.getPokemonList(20) } returns RepoResult.Success(emptyList())
        viewModel = ListPokemonViewModel(repository)

        viewModel.nextPage()
        advanceUntilIdle()

        // When
        viewModel.backToList()
        advanceUntilIdle()

        // Then
        coVerify { repository.getPokemonList(20) }
    }

    @Test
    fun `Given details success When fetchPokemonDetails Then emits SuccessDetails`() = runTest(dispatcher) {
        // Given
        val details = PokemonFull(25, "Pikachu", imageUrl = "https://img", heightMeters =  0.4)
        coEvery { repository.getPokemonInfo(25) } returns RepoResult.Success(details)
        coEvery { repository.getPokemonList(any()) } returns RepoResult.Success(emptyList()) // init

        viewModel = ListPokemonViewModel(repository)

        // When
        viewModel.fetchPokemonDetails(25)

        // Then
        viewModel.state.test {
            // skip init emissions
            skipItems(2) // Loading -> SuccessList (empty)

            // now we should see: Loading -> SuccessDetails
            assertTrue(awaitItem() is ListPokemonState.Loading)

            val detailsState = awaitItem()
            assertTrue(detailsState is ListPokemonState.SuccessDetails)
            assertEquals("Pikachu", (detailsState as ListPokemonState.SuccessDetails).pokemon.name)

            cancelAndIgnoreRemainingEvents()
        }

        coVerify { repository.getPokemonInfo(25) }
    }

}
