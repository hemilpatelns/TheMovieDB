package com.example.tmdb.navigation.listing

import androidx.compose.runtime.Composable
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.entryProvider
import com.example.tmdb.domain.util.VideoType
import com.example.tmdb.navigation.AppKey
import java.util.Map.entry

//@Composable
//fun listingEntryProvider(
//    mainBackStack: NavBackStack<*>,
//    listingBackStack: NavBackStack<ListingKey>
//) = entryProvider {
//
//
//    entry<ListingKey.AllVideos> {
//        AllCardsScreen(
//            onCardClick = { id, type ->
//                when(type) {
//                    VideoType.Movie -> mainBackStack.push(AppKey.MovieDetails(id))
//                    VideoType.Series -> mainBackStack.push(AppKey.SeriesDetails(id))
//                }
//            },
//            onSearchClick = { mainBackStack.push(com.example.app.navigation.AppKey.Search) }
//        )
//    }
//
//
//    entry<ListingKey.Favorites> {
//        FavoritesScreen(
//            onCardClick = { id -> mainBackStack.push(com.example.app.navigation.AppKey.Details(id)) }
//        )
//    }
//}