package com.example.tmdb.movieList.data.mappers

import com.example.tmdb.movieList.data.remote.respond.SeriesDto
import com.example.tmdb.movieList.domain.model.Series

fun SeriesDto.toSeries(): Series{
    return Series(
        adult = adult ?: false,
        backdropPath = backdropPath ?: "",
        firstAirDate = firstAirDate ?: "",
        genreIds = genreIds.orEmpty(),
        id = id ?: -1,
        name = name ?: "",
        originCountry = originCountry.orEmpty(),
        originalLanguage = originalLanguage ?: "",
        originalName = originalName ?: "",
        overview = overview ?: "",
        popularity = popularity ?: 0.0,
        posterPath = poster_path ?: "",
        voteAverage = voteAverage ?: 0.0,
        voteCount = voteCount ?: -1,
        category = "",
    )
}