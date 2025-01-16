package com.example.tmdb.movieList.util

fun String.toTitleCase(): String {
    return this.split("_") // Split by underscore
        .joinToString(" ") { it.lowercase().replaceFirstChar(Char::uppercase) } // Capitalize each word
}