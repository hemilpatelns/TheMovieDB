package com.example.tmdb.presentation.list

import android.R.attr.category
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.tmdb.domain.util.FunctionUtil
import com.example.tmdb.domain.util.MovieCategory
import com.example.tmdb.domain.util.Screen
import com.example.tmdb.presentation.list.components.CategorySelector
import com.example.tmdb.presentation.list.components.ProfileSection
import com.example.tmdb.presentation.list.components.SearchSection
import com.example.tmdb.ui.theme.gradientBrushOne

@Composable
fun VideoListScreen(navController: NavHostController){
    val movieListViewModel = hiltViewModel<MovieListViewModel>()
    val movieListState by movieListViewModel.movieListState.collectAsState()
    val seriesListViewModel = hiltViewModel<SeriesViewModel>()
    val seriesListState by seriesListViewModel.seriesListState.collectAsState()

    VideoList(
        navController = navController,
        movieListState = movieListState,
        seriesListState = seriesListState,
        onUserAction = { action ->
            when(action){
                is VideoListScreenActions.OnSearchClick -> {
                    navController.navigate(Screen.Search.rout)
                }
                is VideoListScreenActions.OnVideoCardClick -> {
                    navController.navigate(Screen.Details.rout + "/${action.id}")
                }
                is VideoListScreenActions.OnToggleVideoCardUi -> {
                    when(action.type) {
                        "movie" -> movieListViewModel.onMovieLongClick(action.category, action.id)
//                        "series" -> seriesListViewModel.toggleFavorite(action.id)
                    }
                }
                is VideoListScreenActions.OnFavoriteButtonClick -> {
                    when(action.type) {
                        "movie" -> movieListViewModel.toggleFavorite(action.id)
                        "series" -> seriesListViewModel.toggleFavorite(action.id)
                    }
                }
                is VideoListScreenActions.OnListEnd -> {
                    when(action.type) {
                        "movie" -> movieListViewModel.paginateList(action.category)
                        "series" -> seriesListViewModel.paginateList(action.category)
                    }
                }
            }
        }
    )
}

@Composable
fun VideoList(
    navController: NavHostController,
    movieListState: MovieListState,
    seriesListState: SeriesListState,
    onUserAction: (VideoListScreenActions) -> Unit
) {
    val isEdgeToEdge = FunctionUtil.isEdgeToEdgeEnabled(LocalView.current)
    var selectedCategory by remember {
        mutableStateOf("Movies")
    }
    val context = LocalContext.current
    Scaffold(){ content ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(gradientBrushOne),
            contentPadding = PaddingValues(bottom = if (isEdgeToEdge) content.calculateBottomPadding() else 0.dp)
        ) {
            item {
                ProfileSection()
            }

            item {
                SearchSection(
                    onSearchClick = {
                        onUserAction(VideoListScreenActions.OnSearchClick)
                    }
                )
            }

            item {
                CategorySelector(
                    navController = navController,
                    movieListState = movieListState,
                    seriesListState = seriesListState,
                    categories = listOf("Movies", "Series", "Novels", "Documentaries"),
                    onCategorySelected = {
                        selectedCategory = it
                    },
                    onListEnd = { category, type ->
                        onUserAction(VideoListScreenActions.OnListEnd(category, type))
                    },
                    onToggleVideoCardUi = { type, category, id ->
                        onUserAction(VideoListScreenActions.OnToggleVideoCardUi(type, category, id))
                    },
                    onToggleFavorite = {id, type ->
                        onUserAction(VideoListScreenActions.OnFavoriteButtonClick(id, type))
                    }
                )
            }
        }
    }
}

sealed interface VideoListScreenActions{
    data object OnSearchClick: VideoListScreenActions
    data class OnVideoCardClick(val id: Int): VideoListScreenActions
    data class OnToggleVideoCardUi(val type: String, val category: String, val id: Int): VideoListScreenActions
    data class OnFavoriteButtonClick(val id: Int, val type: String): VideoListScreenActions
    data class OnListEnd(val category: String, val type: String): VideoListScreenActions
}
