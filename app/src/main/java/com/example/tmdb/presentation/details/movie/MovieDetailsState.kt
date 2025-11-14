package com.example.tmdb.presentation.details.movie

import com.example.tmdb.domain.model.Movie

data class MovieDetailsState(
    val isLoading: Boolean = false,
    val movie: Movie? = null,
    val isFavorite: Boolean = false
)