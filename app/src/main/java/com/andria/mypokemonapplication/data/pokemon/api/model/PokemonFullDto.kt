// data/remote/model/PokemonFullDto.kt
package com.andria.mypokemonapplication.data.pokemon.api.model
import com.google.gson.annotations.SerializedName

data class PokemonFullDto(
    val id: Int,
    val name: String,
    val height: Int, // in decimetres
    val sprites: SpritesDto
)

data class SpritesDto(
    @SerializedName("front_default") val frontDefault: String?,
    val other: OtherSprites? = null
)

data class OtherSprites(
    @SerializedName("official-artwork") val officialArtwork: OfficialArtwork? = null
)

data class OfficialArtwork(
    @SerializedName("front_default") val frontDefault: String?
)
