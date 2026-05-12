package com.example.tmdb.navigation

import androidx.navigation3.runtime.NavKey
import com.example.tmdb.domain.util.VideoType

sealed interface AppKey : NavKey {
    data object Home : AppKey
    data object Listing: AppKey
    data class MovieDetails(val id: Int) : AppKey
    data class SeriesDetails(val id: Int) : AppKey
    data object Search: AppKey
}