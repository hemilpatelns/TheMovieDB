package com.example.tmdb.data.remote

import com.example.tmdb.BuildConfig
import com.example.tmdb.data.remote.respond.MovieDto
import com.example.tmdb.data.remote.respond.MovieListDto
import com.example.tmdb.data.remote.respond.SeriesDto
import com.example.tmdb.data.remote.respond.SeriesListDto
import com.example.tmdb.domain.model.SearchResponse
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface CommonApi {

    companion object{
        const val BASE_URL = "https://api.themoviedb.org/3/"
        const val IMAGE_BASE_URL = "https://image.tmdb.org/t/p/w500"
        const val API_KEY = BuildConfig.API_KEY
    }

    @GET("movie/{category}")
    suspend fun getMovieList(
        @Path("category") category: String,
        @Query("page") page: Int,
        @Query("api_key") apiKey: String = API_KEY
    ): MovieListDto

    @GET("movie/{movie_id}")
    suspend fun getMovieDetailsById(
        @Path("movie_id") movieId: Int,
        @Query("append_to_response") appendToResponse: String = "credits",
        @Query("api_key") apiKey: String = API_KEY
    ): MovieDto

    @GET("tv/{category}")
    suspend fun getSeriesList(
        @Path("category") category: String,
        @Query("page") page: Int,
        @Query("api_key") apiKey: String = API_KEY
    ): SeriesListDto

    @GET("tv/{series_id}")
    suspend fun getSeriesDetailsById(
        @Path("series_id") seriesId: Int,
        @Query("append_to_response") appendToResponse: String = "credits",
        @Query("api_key") apiKey: String = API_KEY
    ): SeriesDto

    @GET("search/multi")
    suspend fun getSearchResults(
        @Query("query") query: String,
        @Query("page") page: Int,
        @Query("include_adult") includeAdult: Boolean = false,
        @Query("api_key") apiKey: String = API_KEY
    ): SearchResponse
}