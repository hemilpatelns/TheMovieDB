package com.example.tmdb.domain.util

object MovieCategory {
    const val NOW_PLAYING = "now_playing"
    const val POPULAR = "popular"
    const val TOP_RATED = "top_rated"
    const val UPCOMING = "upcoming"
}

object SeriesCategory {
    const val AIRING_TODAY = "airing_today"
    const val ON_THE_AIR = "on_the_air"
    const val POPULAR = "popular"
    const val TOP_RATED = "top_rated"
}

object Constants {
    var VIDEO_TYPE = ""
}

enum class VideoType {
    Movie,
    Series,
    Anime,
}