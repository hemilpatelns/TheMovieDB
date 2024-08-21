package com.example.tmdb.details.presentation

import com.example.tmdb.movieList.domain.model.Movie

data class MovieDetailsState(
    val isLoading: Boolean = false,
    val movie: Movie? = null
)