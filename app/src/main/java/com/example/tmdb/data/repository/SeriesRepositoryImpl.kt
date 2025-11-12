package com.example.tmdb.data.repository

import com.example.tmdb.data.local.movie.FavoriteVideoEntity
import com.example.tmdb.data.local.movie.MovieDatabase
import com.example.tmdb.data.mappers.toSeries
import com.example.tmdb.data.remote.CommonApi
import com.example.tmdb.domain.model.Series
import com.example.tmdb.domain.repository.SeriesRepository
import com.example.tmdb.domain.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject


class SeriesRepositoryImpl @Inject constructor(
    private val commonApi: CommonApi,
    private val movieDatabase: MovieDatabase
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

            emit(
                Resource.Success(
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
        seriesId: Int
    ): Flow<Resource<Series>> {
        return flow {
            emit(Resource.Loading(true))
            val seriesFromApi = try {
                commonApi.getSeriesDetailsById(seriesId)
            } catch (e: Exception){
                e.printStackTrace()
                emit(Resource.Error(message = "Error loading series"))
                return@flow
            }
            emit(
                Resource.Success(
                    seriesFromApi.toSeries()
                )
            )
            emit(Resource.Loading(false))
        }
    }

    override suspend fun addFavorite(seriesId: Int) {
        movieDatabase.movieDao.addFavoriteVideo(FavoriteVideoEntity(videoId = seriesId, type = "series"))
    }

    override suspend fun removeFavorite(seriesId: Int) {
        movieDatabase.movieDao.removeFavoriteSeries(seriesId)
    }

    override suspend fun isFavorite(seriesId: Int): Flow<Boolean> {
        return movieDatabase.movieDao.isFavoriteSeries(seriesId)
    }

    override suspend fun getFavoriteSeries(): Flow<Resource<List<Series>>> {
        return flow {
            emit(Resource.Loading(true))
            val seriesIds = movieDatabase.movieDao.getFavoriteSeriesIds().first()
            if (seriesIds.isEmpty()) {
                emit(Resource.Success(emptyList()))
                return@flow
            }
            try {
                val series = seriesIds.map { seriesId ->
                    commonApi.getSeriesDetailsById(seriesId)
                }
                emit(
                    Resource.Success(
                    series.map {
                        it.toSeries()
                    }
                ))
            } catch (e: Exception) {
                e.printStackTrace()
                emit(Resource.Error(message = "Error loading movies"))
                return@flow
            }
        }
    }

    override suspend fun getFavoriteSeriesIds(): Flow<Set<Int>> {
        return movieDatabase.movieDao.getFavoriteSeriesIds().map { it.toSet() }
    }
}