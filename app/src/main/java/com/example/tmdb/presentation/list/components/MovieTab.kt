package com.example.tmdb.presentation.list.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import com.example.tmdb.domain.util.MovieCategory
import com.example.tmdb.presentation.list.MovieListState

@Composable
fun MovieTab(
    navController: NavHostController,
    movieListState: MovieListState,
    nowPlayingListState: LazyListState,
    popularListState: LazyListState,
    topRatedListState: LazyListState,
    upcomingListState: LazyListState,
    onListEnd: (String, String) -> Unit,
    onToggleFavorite: (Int, String) -> Unit
) {
//    val nowPlayingListState = rememberLazyListState()
//    val popularListState = rememberLazyListState()
//    val topRatedListState = rememberLazyListState()
//    val upcomingListState = rememberLazyListState()
    Column {
        MovieVideoList(
            videoType = MovieCategory.NOW_PLAYING,
            movieListState = movieListState,
            navController = navController,
            lazyListState = nowPlayingListState,
            onToggleFavorite = onToggleFavorite,
            onListEnd = onListEnd
        )
        MovieVideoList(
            videoType = MovieCategory.POPULAR,
            movieListState = movieListState,
            navController = navController,
            lazyListState = popularListState,
            onToggleFavorite = onToggleFavorite,
            onListEnd = onListEnd
        )
        MovieVideoList(
            videoType = MovieCategory.TOP_RATED,
            movieListState = movieListState,
            navController = navController,
            lazyListState = topRatedListState,
            onToggleFavorite = onToggleFavorite,
            onListEnd = onListEnd
        )
        MovieVideoList(
            videoType = MovieCategory.UPCOMING,
            movieListState = movieListState,
            navController = navController,
            lazyListState = upcomingListState,
            onToggleFavorite = onToggleFavorite,
            onListEnd = onListEnd
        )
    }
}