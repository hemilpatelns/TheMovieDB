package com.example.tmdb.presentation.list.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.Composable
import com.example.tmdb.domain.util.MovieCategory
import com.example.tmdb.presentation.list.MovieListState

@Composable
fun MovieTab(
    movieListState: MovieListState,
    nowPlayingListState: LazyListState,
    popularListState: LazyListState,
    topRatedListState: LazyListState,
    upcomingListState: LazyListState,
    onToggleVideoCardUi: (String, String, Int) -> Unit,
    onListEnd: (String, String) -> Unit,
    onToggleFavorite: (Int, String) -> Unit,
    onMovieClick: (Int) -> Unit
) {
    Column {
        MovieVideoList(
            videoType = MovieCategory.NOW_PLAYING,
            movieListState = movieListState,
            lazyListState = nowPlayingListState,
            onToggleVideoCardUi = { type, id ->
                onToggleVideoCardUi(type, MovieCategory.NOW_PLAYING, id)
            },
            onToggleFavorite = onToggleFavorite,
            onListEnd = onListEnd,
            onMovieClick = onMovieClick
        )
        MovieVideoList(
            videoType = MovieCategory.POPULAR,
            movieListState = movieListState,
            lazyListState = popularListState,
            onToggleVideoCardUi = { type, id ->
                onToggleVideoCardUi(type, MovieCategory.POPULAR, id)
            },
            onToggleFavorite = onToggleFavorite,
            onListEnd = onListEnd,
            onMovieClick = onMovieClick
        )
        MovieVideoList(
            videoType = MovieCategory.TOP_RATED,
            movieListState = movieListState,
            lazyListState = topRatedListState,
            onToggleVideoCardUi = { type, id ->
                onToggleVideoCardUi(type, MovieCategory.TOP_RATED, id)
            },
            onToggleFavorite = onToggleFavorite,
            onListEnd = onListEnd,
            onMovieClick = onMovieClick
        )
        MovieVideoList(
            videoType = MovieCategory.UPCOMING,
            movieListState = movieListState,
            lazyListState = upcomingListState,
            onToggleVideoCardUi = { type, id ->
                onToggleVideoCardUi(type, MovieCategory.UPCOMING, id)
            },
            onToggleFavorite = onToggleFavorite,
            onListEnd = onListEnd,
            onMovieClick = onMovieClick
        )
    }
}
