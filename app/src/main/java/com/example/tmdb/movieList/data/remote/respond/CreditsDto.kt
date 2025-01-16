package com.example.tmdb.movieList.data.remote.respond


import kotlinx.serialization.*
import kotlinx.serialization.Serializable

@Serializable
data class CreditsDto(
    @SerialName("cast")
    val cast: List<Cast>,
    @SerialName("crew")
    val crew: List<Crew>,
    @SerialName("id")
    val id: Int
)