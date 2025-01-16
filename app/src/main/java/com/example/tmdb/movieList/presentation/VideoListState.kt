package com.example.tmdb.movieList.presentation

import com.example.tmdb.movieList.domain.model.Movie
import com.example.tmdb.movieList.domain.model.Series

sealed class VideoListState

data class MovieListState(
    val isLoading: Boolean = false,
    val nowPlayingMovieListPage: Int = 1,
    val popularMovieListPage: Int = 1,
    val topRatedMovieListPage: Int = 1,
    val upcomingMovieListPage: Int = 1,
    val nowPlayingMovieList: List<Movie> = emptyList(),
    val popularMovieList: List<Movie> = emptyList(),
    val topRatedMovieList: List<Movie> = emptyList(),
    val upcomingMovieList: List<Movie> = emptyList()
)

data class SeriesListState(
    val isLoading: Boolean = false,
    val airingTodaySeriesListPage: Int = 1,
    val onTheAirSeriesListPage: Int = 1,
    val popularSeriesListPage: Int = 1,
    val topRatedSeriesListPage: Int = 1,
    val airingTodaySeriesList: List<Series> = emptyList(),
    val popularSeriesList: List<Series> = emptyList(),
    val onTheAirSeriesList: List<Series> = emptyList(),
    val topRatedSeriesList: List<Series> = emptyList()
)