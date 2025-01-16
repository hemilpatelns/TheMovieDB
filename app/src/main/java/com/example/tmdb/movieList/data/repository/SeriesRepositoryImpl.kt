package com.example.tmdb.movieList.data.repository

import com.example.tmdb.movieList.data.mappers.toSeries
import com.example.tmdb.movieList.data.remote.CommonApi
import com.example.tmdb.movieList.domain.model.Series
import com.example.tmdb.movieList.domain.repository.SeriesRepository
import com.example.tmdb.movieList.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject


class SeriesRepositoryImpl @Inject constructor(
    private val commonApi: CommonApi
): SeriesRepository {
    override suspend fun getSeriesList(
        category: String,
        page: Int
    ): Flow<Resource<List<Series>>> {
        return flow {
            emit(Resource.Loading(true))

            val seriesListFromApi = try {
                commonApi.getSeriesList(category, page)
            } catch (e: Exception){
                e.printStackTrace()
                emit(Resource.Error(message = "Error loading series"))
                return@flow
            }

            emit(Resource.Success(
                seriesListFromApi.results.let {
                    it.map { seriesDto ->
                        seriesDto.toSeries()
                    }
                }
            ))

            emit(Resource.Loading(false))
        }
    }

    override suspend fun getSeries(
        id: Int
    ): Flow<Resource<Series>> {
        return flow {

        }
    }
}