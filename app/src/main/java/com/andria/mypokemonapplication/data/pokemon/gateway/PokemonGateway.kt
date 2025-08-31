package com.andria.mypokemonapplication.data.pokemon.gateway

import com.andria.mypokemonapplication.data.pokemon.api.PokemonApi
import com.andria.mypokemonapplication.domain.pokemon.PokemonDomainError
import com.andria.mypokemonapplication.domain.pokemon.model.Pokemon
import com.andria.mypokemonapplication.domain.pokemon.repository.PokemonRepository
import com.andria.mypokemonapplication.domain.pokemon.repository.RepoResult
import javax.inject.Inject

class PokemonGateway @Inject constructor(
    private val api: PokemonApi,
): PokemonRepository {

    override suspend fun getPokemonList(offset: Int): RepoResult<List<Pokemon>> {
        return runCatching {
            api.getPokemonPage(offset = offset, limit = 50)
        }.fold(
            onSuccess = { response ->
                if (response.isSuccessful) {
                    response.body()?.let {
                        RepoResult.Success(PokemonMapper.map(it))
                    } ?: RepoResult.Failure(PokemonDomainError.Unknown(Exception("Empty body")))
                } else {
                    when (response.code()) {
                        404 -> RepoResult.Failure(PokemonDomainError.NotFound)
                        else -> RepoResult.Failure(PokemonDomainError.Unknown(Exception("HTTP ${response.code()}")))
                    }
                }
            },
            onFailure = { throwable ->
                when (throwable) {
                    is java.io.IOException -> RepoResult.Failure(PokemonDomainError.Network)
                    else -> RepoResult.Failure(PokemonDomainError.Unknown(throwable))
                }
            }
        )
    }

    override suspend fun getPokemonInfo(pokemonId: Int): RepoResult<Pokemon> {
        TODO("Not yet implemented")
    }
}