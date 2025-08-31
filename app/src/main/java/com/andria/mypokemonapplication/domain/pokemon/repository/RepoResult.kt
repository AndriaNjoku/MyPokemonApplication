package com.andria.mypokemonapplication.domain.pokemon.repository

import com.andria.mypokemonapplication.domain.pokemon.PokemonDomainError

sealed class RepoResult<out T> {
    data class Success<T>(val data: T) : RepoResult<T>()
    data class Failure(val error: PokemonDomainError) : RepoResult<Nothing>()
}
