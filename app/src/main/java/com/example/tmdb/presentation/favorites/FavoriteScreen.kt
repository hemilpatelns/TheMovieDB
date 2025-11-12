package com.example.tmdb.presentation.favorites

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.tmdb.domain.util.FunctionUtil
import com.example.tmdb.domain.util.Screen
import com.example.tmdb.presentation.components.VideoCard
import kotlinx.coroutines.launch

@Composable
fun FavoriteScreen(navController: NavHostController) {
    TabLayout(navController)
}

@Composable
fun TabLayout(navController: NavHostController) {
    val isEdgeToEdge = FunctionUtil.isEdgeToEdgeEnabled(LocalView.current)
    val tabs = listOf("Movies", "Series")
    val pagerState = rememberPagerState(pageCount = { tabs.size })
    val favoriteListViewModel = hiltViewModel<FavoriteListViewModel>()
    val favoriteListState by favoriteListViewModel.favoriteListState.collectAsState()
    val coroutineScope = rememberCoroutineScope()
    val favoriteMovieListState = rememberLazyGridState()
    val favoriteSeriesListState = rememberLazyGridState()

    LaunchedEffect(Unit) {
        favoriteListViewModel.getFavoriteMovies()
        favoriteListViewModel.getFavoriteSeries()
    }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        // Tabs
        TabRow(selectedTabIndex = pagerState.currentPage) {
            tabs.forEachIndexed { index, title ->
                Tab(
                    modifier = Modifier
                        .padding(top = if (isEdgeToEdge) 40.dp else 0.dp),
                    selected = pagerState.currentPage == index,
                    onClick = {
                        coroutineScope.launch {
                            pagerState.animateScrollToPage(index)
                        }
                    },
                    text = { Text(text = title) }
                )
            }
        }

        // Pager for swiping
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.fillMaxSize(),
            verticalAlignment = Alignment.Top
        ) { page ->
            when (page) {
                0 -> FavoriteMovieList(
                    movieListState = favoriteListState,
                    navController = navController,
                    lazyGridState = favoriteMovieListState,
                    onToggleFavorite = {
                        favoriteListViewModel.toggleFavorite(it, "movie")
                    }
                )
                1 -> FavoriteSeriesList(
                    seriesListState = favoriteListState,
                    navController = navController,
                    lazyGridState = favoriteSeriesListState,
                    onToggleFavorite = {
                        favoriteListViewModel.toggleFavorite(it, "series")
                    }
                )
            }
        }
    }
}

@Composable
fun FavoriteMovieList(
    movieListState: FavoriteListState,
    navController: NavHostController,
    lazyGridState: LazyGridState,
    onToggleFavorite: (Int) -> Unit
) {
    val movieList = movieListState.favoriteMovieList
    if (movieList.isEmpty() && movieListState.isLoading) {
        // Show loading indicator when the list is empty and loading
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    } else {
        // Display the list of videos
        LazyVerticalGrid(
            state = lazyGridState,
            columns = GridCells.Fixed(2),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp),
            horizontalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            items(movieList.size) { index ->
                VideoCard(
                    navController = navController,
                    width = 200.dp,
                    height = 250.dp,
                    poster = movieList[index].poster_path,
                    id = movieList[index].id,
                    title = movieList[index].title,
                    isFavorite = movieList[index].isFavorite,
                    route = Screen.Details.rout,
                    onToggleFavorite = onToggleFavorite,
                    onVideoCardClick = {}
                )
            }
        }
    }
}

@Composable
fun FavoriteSeriesList(
    seriesListState: FavoriteListState,
    navController: NavHostController,
    lazyGridState: LazyGridState,
    onToggleFavorite: (Int) -> Unit
) {
    val seriesList = seriesListState.favoriteSeriesList
    if (seriesList.isEmpty() && seriesListState.isLoading) {
        // Show loading indicator when the list is empty and loading
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    } else {
        // Display the list of videos
        LazyVerticalGrid(
            state = lazyGridState,
            columns = GridCells.Fixed(2),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp),
            horizontalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            items(seriesList.size) { index ->
                VideoCard(
                    navController = navController,
                    width = 200.dp,
                    height = 250.dp,
                    poster = seriesList[index].posterPath,
                    id = seriesList[index].id,
                    title = seriesList[index].name,
                    isFavorite = seriesList[index].isFavorite,
                    route = Screen.SeriesDetails.rout,
                    onToggleFavorite = onToggleFavorite,
                    onVideoCardClick = {}
                )
            }
        }
    }
}