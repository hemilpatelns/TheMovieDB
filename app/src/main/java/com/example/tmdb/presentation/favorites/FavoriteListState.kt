package com.example.tmdb.presentation.favorites

import com.example.tmdb.domain.model.Movie
import com.example.tmdb.domain.model.Series

data class FavoriteListState(
    val isLoading: Boolean = false,
    val favoriteMovieList: List<Movie> = emptyList(),
    val favoriteSeriesList: List<Series> = emptyList()
)
