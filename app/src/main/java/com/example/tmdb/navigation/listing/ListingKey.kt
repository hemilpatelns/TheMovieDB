package com.example.tmdb.navigation.listing

import androidx.navigation3.runtime.NavKey

sealed interface ListingKey : NavKey{
    data object AllVideos: ListingKey
    data object Favorites: ListingKey
}