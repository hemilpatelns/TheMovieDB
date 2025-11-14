package com.example.tmdb.presentation.list.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import com.example.tmdb.domain.util.SeriesCategory
import com.example.tmdb.presentation.list.SeriesListState

@Composable
fun SeriesTab(
    navController: NavHostController,
    seriesListState: SeriesListState,
    airingTodayListState: LazyListState,
    onTheAirListState: LazyListState,
    popularListState: LazyListState,
    topRatedListState: LazyListState,
    onToggleFavorite: (Int, String) -> Unit,
    onListEnd: (String, String) -> Unit
) {
    Column {
        SeriesVideoList(
            SeriesCategory.AIRING_TODAY,
            seriesListState = seriesListState,
            navController = navController,
            lazyListState = airingTodayListState,
            onToggleFavorite = onToggleFavorite,
            onListEnd = onListEnd
        )
        SeriesVideoList(
            SeriesCategory.ON_THE_AIR,
            seriesListState = seriesListState,
            navController = navController,
            lazyListState = onTheAirListState,
            onToggleFavorite = onToggleFavorite,
            onListEnd = onListEnd
        )
        SeriesVideoList(
            SeriesCategory.POPULAR,
            seriesListState = seriesListState,
            navController = navController,
            lazyListState = popularListState,
            onToggleFavorite = onToggleFavorite,
            onListEnd = onListEnd
        )
        SeriesVideoList(
            SeriesCategory.TOP_RATED,
            seriesListState = seriesListState,
            navController = navController,
            lazyListState = topRatedListState,
            onToggleFavorite = onToggleFavorite,
            onListEnd = onListEnd
        )
    }
}