package com.example.tmdb.data.local.movie

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface MovieDao {
    @Upsert
    suspend fun upsertMovieList(movieList: List<MovieEntity>)

    @Query("SELECT * FROM MovieEntity WHERE id = :id")
    suspend fun getMovieById(id: Int): MovieEntity?

    @Query("SELECT * FROM MovieEntity WHERE category = :category")
    suspend fun getMovieListByCategory(category: String): List<MovieEntity>

    @Query("SELECT * FROM MovieEntity WHERE category = 'popular' ORDER BY popularity DESC")
    suspend fun getPopularMovieList(): List<MovieEntity>

    @Query("SELECT * FROM MovieEntity WHERE vote_average = 0")
    suspend fun getUpcomingMovieList(): List<MovieEntity>

    // Favorite Videos
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addFavoriteVideo(favorite: FavoriteVideoEntity)

    @Query("DELETE FROM favorite_videos WHERE videoId = :videoId AND type = 'movie'")
    suspend fun removeFavoriteMovie(videoId: Int)

    @Query("DELETE FROM favorite_videos WHERE videoId = :videoId AND type = 'series'")
    suspend fun removeFavoriteSeries(videoId: Int)

    @Query("SELECT EXISTS(SELECT * FROM favorite_videos WHERE videoId = :videoId AND type = 'movie')")
    fun isFavoriteMovie(videoId: Int): Flow<Boolean>

    @Query("SELECT EXISTS(SELECT * FROM favorite_videos WHERE videoId = :videoId AND type = 'series')")
    fun isFavoriteSeries(videoId: Int): Flow<Boolean>

    @Query("SELECT videoId FROM favorite_videos WHERE type = 'movie'")
    fun getFavoriteMovieIds(): Flow<List<Int>>

    @Query("SELECT videoId FROM favorite_videos WHERE type = 'series'")
    fun getFavoriteSeriesIds(): Flow<List<Int>>
}