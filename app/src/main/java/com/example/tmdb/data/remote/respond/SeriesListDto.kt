package com.example.tmdb.data.remote.respond


import com.google.gson.annotations.SerializedName

data class SeriesListDto(
    @SerializedName("page")
    val page: Int,
    @SerializedName("results")
    val results: List<SeriesDto>,
    @SerializedName("total_pages")
    val totalPages: Int,
    @SerializedName("total_results")
    val totalResults: Int
)