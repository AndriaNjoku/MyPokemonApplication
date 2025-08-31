package com.andria.mypokemonapplication.di

import com.andria.mypokemonapplication.data.pokemon.api.PokemonApi
import com.andria.mypokemonapplication.data.pokemon.gateway.PokemonGateway
import com.andria.mypokemonapplication.domain.pokemon.repository.PokemonRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds
    abstract fun bindPokemonRepository(
        impl: PokemonGateway
    ): PokemonRepository
}

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    fun providePokemonApi(retrofit: Retrofit): PokemonApi =
        retrofit.create(PokemonApi::class.java)

    @Provides
    fun provideRetrofit(): Retrofit = Retrofit.Builder()
        .baseUrl("https://pokeapi.co/api/v2/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

}
