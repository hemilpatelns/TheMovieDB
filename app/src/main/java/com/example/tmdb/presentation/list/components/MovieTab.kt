package com.example.tmdb.presentation.list.components

import android.R.attr.type
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import com.example.tmdb.domain.util.MovieCategory
import com.example.tmdb.presentation.list.MovieListState
import com.example.tmdb.presentation.list.VideoListScreenActions

@Composable
fun MovieTab(
    navController: NavHostController,
    movieListState: MovieListState,
    nowPlayingListState: LazyListState,
    popularListState: LazyListState,
    topRatedListState: LazyListState,
    upcomingListState: LazyListState,
    onToggleVideoCardUi: (String, String, Int) -> Unit,
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
            onToggleVideoCardUi = { type, id ->
                onToggleVideoCardUi(type, MovieCategory.NOW_PLAYING, id)
            },
            onToggleFavorite = onToggleFavorite,
            onListEnd = onListEnd
        )
        MovieVideoList(
            videoType = MovieCategory.POPULAR,
            movieListState = movieListState,
            navController = navController,
            lazyListState = popularListState,
            onToggleVideoCardUi = { type, id ->
                onToggleVideoCardUi(type, MovieCategory.POPULAR, id)
            },
            onToggleFavorite = onToggleFavorite,
            onListEnd = onListEnd
        )
        MovieVideoList(
            videoType = MovieCategory.TOP_RATED,
            movieListState = movieListState,
            navController = navController,
            lazyListState = topRatedListState,
            onToggleVideoCardUi = { type, id ->
                onToggleVideoCardUi(type, MovieCategory.TOP_RATED, id)
            },
            onToggleFavorite = onToggleFavorite,
            onListEnd = onListEnd
        )
        MovieVideoList(
            videoType = MovieCategory.UPCOMING,
            movieListState = movieListState,
            navController = navController,
            lazyListState = upcomingListState,
            onToggleVideoCardUi = { type, id ->
                onToggleVideoCardUi(type, MovieCategory.UPCOMING, id)
            },
            onToggleFavorite = onToggleFavorite,
            onListEnd = onListEnd
        )
    }
}