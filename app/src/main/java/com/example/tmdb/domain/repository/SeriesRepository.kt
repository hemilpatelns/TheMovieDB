package com.example.tmdb.domain.repository

import com.example.tmdb.domain.model.Movie
import com.example.tmdb.domain.model.Series
import com.example.tmdb.domain.util.Resource
import kotlinx.coroutines.flow.Flow

interface SeriesRepository {

    suspend fun getSeriesList(
        category: String,
        page: Int
    ): Flow<Resource<List<Series>>>

    suspend fun getSeries(seriesId: Int) : Flow<Resource<Series>>

    suspend fun addFavorite(seriesId: Int)

    suspend fun removeFavorite(seriesId: Int)

    suspend fun isFavorite(seriesId: Int): Flow<Boolean>

    suspend fun getFavoriteSeries(): Flow<Resource<List<Series>>>

    suspend fun getFavoriteSeriesIds(): Flow<Set<Int>>
}