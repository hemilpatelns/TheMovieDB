package com.example.tmdb.domain.model

import com.example.tmdb.data.remote.respond.Cast
import com.example.tmdb.data.remote.respond.Crew

data class Series(
    val adult: Boolean,
    val backdropPath: String,
    val firstAirDate: String,
    val genreIds: List<Int>,
    val id: Int,
    val name: String,
    val originCountry: List<String>,
    val originalLanguage: String,
    val originalName: String,
    val overview: String,
    val popularity: Double,
    val posterPath: String,
    val voteAverage: Double,
    val voteCount: Int,
    val category: String,
    val cast: List<Cast> = emptyList(),
    val crew: List<Crew> = emptyList(),
    val isFavorite: Boolean = false,
    val showLongClickUi: Boolean = false
): Video()