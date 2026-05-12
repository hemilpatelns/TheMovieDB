package com.example.tmdb.presentation.list.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.tmdb.domain.util.Constants
import com.example.tmdb.presentation.list.MovieListState
import com.example.tmdb.presentation.list.SeriesListState
import com.example.tmdb.presentation.list.VideoListScreenActions

@Composable
fun CategorySelector(
    navController: NavHostController,
    movieListState: MovieListState,
    seriesListState: SeriesListState,
    categories: List<String>,
    onCategorySelected: (String) -> Unit,
    onToggleVideoCardUi: (String, String, Int) -> Unit,
    onToggleFavorite: (Int, String) -> Unit,
    onListEnd: (String, String) -> Unit
) {
    var selectedCategory by rememberSaveable { mutableStateOf(categories.first()) }
    val nowPlayingMoviesListState = rememberLazyListState()
    val popularMoviesListState = rememberLazyListState()
    val topRatedMoviesListState = rememberLazyListState()
    val upcomingMoviesListState = rememberLazyListState()
    val airingTodaySeriesListState = rememberLazyListState()
    val onTheAirSeriesListState = rememberLazyListState()
    val popularSeriesListState = rememberLazyListState()
    val topRatedSeriesListState = rememberLazyListState()

    Column {
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy((-45).dp),
            modifier = Modifier
                .fillMaxWidth()
        ) {
            items(categories) { category ->
                Spacer(modifier = Modifier.padding(start = 30.dp))
                CategoryItem(
                    category = category,
                    isSelected = category == selectedCategory,
                    onClick = {
                        selectedCategory = category
                        onCategorySelected(category)
                        Constants.VIDEO_TYPE = category.lowercase()
                    }
                )
                Spacer(modifier = Modifier.padding(start = 30.dp))
            }
        }
        Box(
            modifier = Modifier
                .fillMaxSize()
        ) {
            when (selectedCategory) {
                "Movies" -> {
                    MovieTab(
                        navController = navController,
                        movieListState = movieListState,
                        nowPlayingListState = nowPlayingMoviesListState,
                        popularListState = popularMoviesListState,
                        topRatedListState = topRatedMoviesListState,
                        upcomingListState = upcomingMoviesListState,
                        onToggleVideoCardUi = onToggleVideoCardUi,
                        onToggleFavorite = onToggleFavorite,
                        onListEnd = onListEnd
                    )
                }

                "Series" -> {
                    SeriesTab(
                        navController = navController,
                        seriesListState = seriesListState,
                        airingTodayListState = airingTodaySeriesListState,
                        onTheAirListState = onTheAirSeriesListState,
                        popularListState = popularSeriesListState,
                        topRatedListState = topRatedSeriesListState,
                        onToggleFavorite = onToggleFavorite,
                        onListEnd = onListEnd
                    )
                }
            }
        }
    }
}