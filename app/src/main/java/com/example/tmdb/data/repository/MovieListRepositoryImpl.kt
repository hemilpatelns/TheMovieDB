package com.example.tmdb.data.repository

import com.example.tmdb.data.local.movie.FavoriteVideoEntity
import com.example.tmdb.data.local.movie.MovieDatabase
import com.example.tmdb.data.mappers.toMovie
import com.example.tmdb.data.remote.CommonApi
import com.example.tmdb.domain.model.Movie
import com.example.tmdb.domain.repository.MovieListRepository
import com.example.tmdb.domain.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import okio.IOException
import retrofit2.HttpException
import javax.inject.Inject

class MovieListRepositoryImpl @Inject constructor(
    private val commonApi: CommonApi,
    private val movieDatabase: MovieDatabase
) : MovieListRepository {

    override suspend fun getMovieList(
//        forceFetchFromRemote: Boolean,
        category: String,
        page: Int
    ): Flow<Resource<List<Movie>>> {
        return flow {
            emit(Resource.Loading(true))

            // Fetching from RoomDB
//            val localMovieList: List<MovieEntity> = when (category) {
//                Category.POPULAR -> movieDatabase.movieDao.getPopularMovieList()
//                Category.UPCOMING -> movieDatabase.movieDao.getUpcomingMovieList()
//                else -> movieDatabase.movieDao.getMovieListByCategory(category)
//            }
//
//            val shouldLoadLocalMovie = localMovieList.isNotEmpty() && !forceFetchFromRemote
//
//            if (shouldLoadLocalMovie) {
//                emit(Resource.Success(
//                    data = localMovieList.map { movieEntity ->
//                        movieEntity.toMovie(category)
//                    }
//                ))
//
//                emit(Resource.Loading(false))
//                return@flow
//            }

            val movieListFromApi = try {
                commonApi.getMovieList(category, page)
            } catch (e: IOException) {
                e.printStackTrace()
                emit(Resource.Error(message = "Error loading movies"))
                return@flow
            } catch (e: HttpException) {
                e.printStackTrace()
                emit(Resource.Error(message = "Error loading movies"))
                return@flow
            } catch (e: Exception) {
                e.printStackTrace()
                emit(Resource.Error(message = "Error loading movies"))
                return@flow
            }

            // Storing to RoomDB
//            val movieEntities = movieListFromApi.results.let {
//                it.map { movieDto ->
//                    movieDto.toMovieEntity(category)
//                }
//            }
//
//            movieDatabase.movieDao.upsertMovieList(movieEntities)

            emit(
                Resource.Success(
                // RoomDBImpl
//                movieEntities.map { it.toMovie(category) }
                movieListFromApi.results.let {
                    it.map { movieDto ->
                        movieDto.toMovie()
                    }
                }
            ))
            emit(Resource.Loading(false))
        }
    }

    // Get movie from RoomDB
    override suspend fun getMovie(id: Int): Flow<Resource<Movie>> {
        return flow {
            emit(Resource.Loading(true))

            val movieEntity = movieDatabase.movieDao.getMovieById(id)

            if (movieEntity != null) {
                emit(
                    Resource.Success(
                        data = movieEntity.toMovie(movieEntity.category)
                    )
                )
                emit(Resource.Loading(false))
                return@flow
            }
            emit(Resource.Error("No such movie found"))
            emit(Resource.Loading(false))
        }
    }

    override suspend fun getMovieFromApi(movieId: Int): Flow<Resource<Movie>> {
        return flow {
            emit(Resource.Loading(true))
            val movieFromApi = try {
                commonApi.getMovieDetailsById(movieId)
            } catch (e: Exception) {
                e.printStackTrace()
                emit(Resource.Error(message = "Error loading movies"))
                return@flow
            }
            emit(
                Resource.Success(
                    movieFromApi.toMovie()
                )
            )
            emit(Resource.Loading(false))
        }
    }

    override suspend fun addFavorite(movieId: Int) {
        movieDatabase.movieDao.addFavoriteVideo(FavoriteVideoEntity(videoId = movieId, type = "movie"))
    }

    override suspend fun removeFavorite(movieId: Int) {
        movieDatabase.movieDao.removeFavoriteMovie(movieId)
    }

    override suspend fun isFavorite(movieId: Int): Flow<Boolean> {
        return movieDatabase.movieDao.isFavoriteMovie(movieId)
    }

    override suspend fun getFavoriteMovies(): Flow<Resource<List<Movie>>> {
        return flow {
            emit(Resource.Loading(true))
            //Getting list of movie ids from the db
            val movieIds = movieDatabase.movieDao.getFavoriteMovieIds().first()
            if (movieIds.isEmpty()) {
                emit(Resource.Success(emptyList()))
                return@flow
            }
            //Getting movies from api with list of movie ids
            try {
                val movies = movieIds.map { movieId ->
                    commonApi.getMovieDetailsById(movieId)
                }
                emit(
                    Resource.Success(
                    movies.map {
                        it.toMovie()
                    }
                ))
            } catch (e: Exception) {
                e.printStackTrace()
                emit(Resource.Error(message = "Error loading movies"))
                return@flow
            }
        }
    }
}