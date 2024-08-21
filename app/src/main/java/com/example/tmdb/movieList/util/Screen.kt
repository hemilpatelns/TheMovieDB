package com.example.tmdb.movieList.util

sealed class Screen(val rout: String) {
    data object Home: Screen("main")
    data object VideoList: Screen("videoList")
    data object Details: Screen("details")
}