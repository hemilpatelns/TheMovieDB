package com.example.tmdb.movieList.data.remote.respond


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SeriesListDto(
    @SerialName("page")
    val page: Int,
    @SerialName("results")
    val results: List<SeriesDto>,
    @SerialName("total_pages")
    val totalPages: Int,
    @SerialName("total_results")
    val totalResults: Int
)