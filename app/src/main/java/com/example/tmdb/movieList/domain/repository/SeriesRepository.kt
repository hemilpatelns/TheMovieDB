package com.example.tmdb.movieList.domain.repository

import com.example.tmdb.movieList.domain.model.Series
import com.example.tmdb.movieList.util.Resource
import kotlinx.coroutines.flow.Flow

interface SeriesRepository {

    suspend fun getSeriesList(
        category: String,
        page: Int
    ): Flow<Resource<List<Series>>>

    suspend fun getSeries(id: Int) : Flow<Resource<Series>>
}