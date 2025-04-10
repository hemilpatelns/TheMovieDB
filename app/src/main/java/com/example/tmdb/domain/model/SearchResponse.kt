package com.example.tmdb.domain.model

import com.google.gson.annotations.SerializedName

data class SearchResponse(
    @SerializedName("page")
    val page: String?,
    @SerializedName("results")
    val results: List<SearchData>?,
    @SerializedName("total_pages")
    val totalPages: Int,
    @SerializedName("total_results")
    val totalResults: Int
)

data class SearchData(
    val id: Int,
    @SerializedName("name")
    val name: String?, // For TV Shows
    @SerializedName("title")
    val title: String?, // For Movies
    @SerializedName("original_name")
    val originalName: String?, // For TV Shows
    @SerializedName("original_title")
    val originalTitle: String?, // For Movies
    @SerializedName("backdrop_path")
    val backdropPath: String?,
    @SerializedName("poster_path")
    val posterPath: String?,
    @SerializedName("media_type")
    val mediaType: String, // "movie" or "tv"
    @SerializedName("adult")
    val adult: Boolean,
    @SerializedName("original_language")
    val originalLanguage: String,
    @SerializedName("genre_ids")
    val genreIds: List<Int>,
    @SerializedName("popularity")
    val popularity: Double,
    @SerializedName("vote_average")
    val voteAverage: Double,
    @SerializedName("vote_count")
    val voteCount: Int,
    @SerializedName("overview")
    val overview: String?,
    @SerializedName("origin_country")
    val originCountry: List<String>? = null, // Present only in TV Shows
    @SerializedName("first_air_date")
    val firstAirDate: String?, // Present only in TV Shows
    @SerializedName("release_date")
    val releaseDate: String?, // Present only in Movies
    @SerializedName("video")
    val video: Boolean? // Present only in Movies
) {
    val formattedTitle: String?
        get() = name ?: title

    val formattedOriginalTitle: String?
        get() = originalName ?: originalTitle

    val formattedDate: String?
        get() = firstAirDate ?: releaseDate
}
