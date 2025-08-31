package com.andria.mypokemonapplication.domain.pokemon

sealed class PokemonDomainError {
    object Network : PokemonDomainError()
    object NotFound : PokemonDomainError()
    data class Unknown(val throwable: Throwable) : PokemonDomainError()
}
