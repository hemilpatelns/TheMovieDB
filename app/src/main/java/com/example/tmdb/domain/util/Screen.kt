package com.example.tmdb.domain.util

import kotlinx.serialization.Serializable

sealed interface Screen {
    @Serializable
    data object Home : Screen

    @Serializable
    data object BottomNav : Screen

    @Serializable
    data object VideoList : Screen

    @Serializable
    data object Favorites : Screen

    @Serializable
    data class Details(val movieId: Int) : Screen

    @Serializable
    data class SeriesDetails(val seriesId: Int) : Screen

    @Serializable
    data object Search : Screen
}
