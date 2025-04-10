package com.example.tmdb.presentation.list

import com.example.tmdb.domain.model.Movie
import com.example.tmdb.domain.model.SearchData
import com.example.tmdb.domain.model.Series

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
    val upcomingMovieList: List<Movie> = emptyList(),
    val favoriteMovieList: List<Movie> = emptyList()
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
    val topRatedSeriesList: List<Series> = emptyList(),
    val favoriteSeriesList: List<Series> = emptyList()
)

data class SearchListState(
    val isLoading: Boolean = false,
    val searchListPage: Int = 1,
    val searchList: List<SearchData> = emptyList()
)