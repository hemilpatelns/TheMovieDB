package com.example.tmdb.data.mappers

import com.example.tmdb.data.local.movie.MovieEntity
import com.example.tmdb.data.remote.respond.Cast
import com.example.tmdb.data.remote.respond.Crew
import com.example.tmdb.data.remote.respond.MovieDto
import com.example.tmdb.domain.model.Movie

fun MovieDto.toMovieEntity(
    category: String
): MovieEntity {
    return MovieEntity(
        adult = adult ?: false,
        backdrop_path = backdrop_path ?: "",
        original_language = original_language ?: "",
        original_title = original_title ?: "",
        overview = overview ?: "",
        popularity = popularity ?: 0.0,
        poster_path = poster_path ?: "",
        release_date = release_date ?: "",
        title = title ?: "",
        video = video ?: false,
        vote_average = vote_average ?: 0.0,
        vote_count = vote_count ?: 0,
        category = category,
        id = id ?: -1,
        genre_ids = try {
            genre_ids?.joinToString(",") ?: "-1, -2"
        } catch (e: Exception) {
            "-1, -2"
        },

    )
}

fun MovieEntity.toMovie(
    category: String
): Movie {
    return Movie(
        adult = adult,
        backdrop_path = backdrop_path,
        id = id,
        original_language = original_language,
        original_title = original_title,
        overview = overview,
        popularity = popularity,
        poster_path = poster_path,
        release_date = release_date,
        title = title,
        video = video,
        vote_average = vote_average,
        vote_count = vote_count,
        category = category,
        genre_ids = try {
            genre_ids.split(",").map { it.toInt() }
        } catch (e: Exception) {
            listOf(-1, -2)
        },
    )
}

fun MovieDto.toMovie(): Movie {
    return Movie(
        adult = adult ?: false,
        backdrop_path = backdrop_path ?: "",
        original_language = original_language ?: "",
        original_title = original_title ?: "",
        overview = overview ?: "",
        popularity = popularity ?: 0.0,
        poster_path = poster_path ?: "",
        release_date = release_date ?: "",
        title = title ?: "",
        video = video ?: false,
        vote_average = vote_average ?: 0.0,
        vote_count = vote_count ?: 0,
        category = "",
        id = id ?: -1,
        genre_ids = genre_ids.orEmpty(),
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