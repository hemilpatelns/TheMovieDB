package com.example.tmdb.data.mappers

import com.example.tmdb.data.remote.respond.Cast
import com.example.tmdb.data.remote.respond.Crew
import com.example.tmdb.data.remote.respond.SeriesDto
import com.example.tmdb.domain.model.Series

fun SeriesDto.toSeries(): Series {
    return Series(
        adult = adult ?: false,
        backdropPath = backdrop_path ?: "",
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
        cast = credits?.cast?.map {
            Cast(
                adult = it.adult ?: false,
                castId = it.castId ?: -1,
                character = it.character ?: "",
                creditId = it.creditId ?: "",
                gender = it.gender,
                id = it.id ?: -1,
                knownForDepartment = it.knownForDepartment ?: "",
                name = it.name ?: "",
                order = it.order,
                originalName = it.originalName ?: "",
                popularity = it.popularity ?: 0.0,
                profile_path = it.profile_path ?: ""
            )
        }?: emptyList(),
        crew = credits?.crew?.map {
            Crew(
                adult = it.adult ?: false,
                creditId = it.creditId ?: "",
                department = it.department ?: "",
                gender = it.gender,
                id = it.id ?: -1,
                job = it.job ?: "",
                knownForDepartment = it.knownForDepartment ?: "",
                name = it.name ?: "",
                originalName = it.originalName ?: "",
                popularity = it.popularity ?: 0.0,
                profilePath = it.profilePath ?: ""
            )
        } ?: emptyList()
    )
}