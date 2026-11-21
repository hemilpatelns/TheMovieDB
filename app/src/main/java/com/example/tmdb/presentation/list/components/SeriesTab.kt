package com.example.tmdb.presentation.list.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.Composable
import com.example.tmdb.domain.util.SeriesCategory
import com.example.tmdb.presentation.list.SeriesListState

@Composable
fun SeriesTab(
    seriesListState: SeriesListState,
    airingTodayListState: LazyListState,
    onTheAirListState: LazyListState,
    popularListState: LazyListState,
    topRatedListState: LazyListState,
    onToggleFavorite: (Int, String) -> Unit,
    onListEnd: (String, String) -> Unit,
    onSeriesClick: (Int) -> Unit
) {
    Column {
        SeriesVideoList(
            videoType = SeriesCategory.AIRING_TODAY,
            seriesListState = seriesListState,
            lazyListState = airingTodayListState,
            onToggleFavorite = onToggleFavorite,
            onListEnd = onListEnd,
            onSeriesClick = onSeriesClick
        )
        SeriesVideoList(
            videoType = SeriesCategory.ON_THE_AIR,
            seriesListState = seriesListState,
            lazyListState = onTheAirListState,
            onToggleFavorite = onToggleFavorite,
            onListEnd = onListEnd,
            onSeriesClick = onSeriesClick
        )
        SeriesVideoList(
            videoType = SeriesCategory.POPULAR,
            seriesListState = seriesListState,
            lazyListState = popularListState,
            onToggleFavorite = onToggleFavorite,
            onListEnd = onListEnd,
            onSeriesClick = onSeriesClick
        )
        SeriesVideoList(
            videoType = SeriesCategory.TOP_RATED,
            seriesListState = seriesListState,
            lazyListState = topRatedListState,
            onToggleFavorite = onToggleFavorite,
            onListEnd = onListEnd,
            onSeriesClick = onSeriesClick
        )
    }
}
