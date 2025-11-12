package com.example.tmdb.domain.repository

import com.example.tmdb.domain.model.Movie
import com.example.tmdb.domain.util.Resource
import kotlinx.coroutines.flow.Flow

interface MovieListRepository {
    suspend fun getMovieList(
//        forceFetchFromRemote: Boolean,
        category: String,
        page:Int
    ): Flow<Resource<List<Movie>>>

    suspend fun getMovie(id: Int): Flow<Resource<Movie>>

    suspend fun getMovieFromApi(
        movieId: Int
    ): Flow<Resource<Movie>>

    suspend fun addFavorite(movieId: Int)

    suspend fun removeFavorite(movieId: Int)

    suspend fun isFavorite(movieId: Int): Flow<Boolean>

    suspend fun getFavoriteMovies(): Flow<Resource<List<Movie>>>

    suspend fun getFavoriteMovieIds(): Flow<Set<Int>>
}