package com.example.tmdb.domain.model

import com.example.tmdb.data.remote.respond.Cast
import com.example.tmdb.data.remote.respond.Crew

data class Movie(
    val adult: Boolean,
    val backdrop_path: String,
    val genre_ids: List<Int>,
    val id: Int,
    val original_language: String,
    val original_title: String,
    val overview: String,
    val popularity: Double,
    val poster_path: String,
    val release_date: String,
    val title: String,
    val video: Boolean,
    val vote_average: Double,
    val vote_count: Int,
    val category: String,
    val cast: List<Cast> = emptyList(),
    val crew: List<Crew> = emptyList(),
    val isFavorite: Boolean = false
): Video()