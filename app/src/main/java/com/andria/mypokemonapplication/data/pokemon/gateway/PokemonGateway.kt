package com.andria.mypokemonapplication.data.pokemon.gateway

import com.andria.mypokemonapplication.data.pokemon.api.PokemonApi
import com.andria.mypokemonapplication.domain.pokemon.PokemonDomainError
import com.andria.mypokemonapplication.domain.pokemon.model.PokemonFull
import com.andria.mypokemonapplication.domain.pokemon.model.PokemonLite
import com.andria.mypokemonapplication.domain.pokemon.repository.PokemonRepository
import com.andria.mypokemonapplication.domain.pokemon.repository.RepoResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class PokemonGateway @Inject constructor(
    private val api: PokemonApi,
): PokemonRepository {

    private val pageCache = mutableMapOf<Int, List<PokemonLite>>()

    override suspend fun getPokemonList(offset: Int): RepoResult<List<PokemonLite>> {

        pageCache[offset]?.let { return RepoResult.Success(it) }


        return withContext(Dispatchers.IO) {
            runCatching {
                api.getPokemonPage(offset = offset, limit = 20)
            }.fold(
                onSuccess = { response ->
                    if (response.isSuccessful) {
                        response.body()?.let {
                            RepoResult.Success(PokemonMapper.map(it)).also { result ->  pageCache[offset] = result.data }
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
    }

    override suspend fun getPokemonInfo(pokemonId: Int): RepoResult<PokemonFull> {
        return withContext(Dispatchers.IO) {
            runCatching {
                api.getPokemon(pokemonId.toString())
            }.fold(
                onSuccess = { response ->
                    if (response.isSuccessful) {
                        response.body()?.let {
                            RepoResult.Success(PokemonMapper.map(response.body()!!))
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
    }
}