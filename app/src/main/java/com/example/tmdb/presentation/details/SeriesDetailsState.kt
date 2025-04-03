package com.example.tmdb.presentation.details

import com.example.tmdb.domain.model.Series

data class SeriesDetailsState(
    val isLoading: Boolean = false,
    val series: Series? = null,
    val isFavorite: Boolean = false
)
