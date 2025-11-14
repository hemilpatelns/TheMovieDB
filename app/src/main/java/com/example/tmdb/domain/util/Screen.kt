package com.example.tmdb.domain.util

sealed class Screen(val rout: String) {
    data object Home: Screen("home")
    data object BottomNav: Screen("bottom_bar")
    data object VideoList: Screen("list")
    data object Details: Screen("details")
    data object SeriesDetails: Screen("series_details")
    data object Favorites: Screen("favorites")
    data object Search: Screen("search")
}