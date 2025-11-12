package com.example.tmdb.presentation.list

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.tmdb.R
import com.example.tmdb.domain.util.Constants
import com.example.tmdb.domain.util.FunctionUtil
import com.example.tmdb.domain.util.MovieCategory
import com.example.tmdb.domain.util.Screen
import com.example.tmdb.domain.util.SeriesCategory
import com.example.tmdb.domain.util.toTitleCase
import com.example.tmdb.presentation.components.VideoCard
import com.example.tmdb.presentation.details.MovieDetailsState
import com.example.tmdb.presentation.details.MovieDetailsViewModel
import com.example.tmdb.ui.theme.gradientBrushOne

@Composable
fun VideoListScreen(navController: NavHostController){
    val movieListViewModel = hiltViewModel<MovieListViewModel>()
    val movieListState by movieListViewModel.movieListState.collectAsState()

    LaunchedEffect(Unit) {
        movieListViewModel.getMovieList(MovieCategory.NOW_PLAYING)
        movieListViewModel.getMovieList(MovieCategory.POPULAR)
        movieListViewModel.getMovieList(MovieCategory.TOP_RATED)
        movieListViewModel.getMovieList(MovieCategory.UPCOMING)
    }

    VideoList(
        navController = navController,
        movieListState = movieListState,
        onUserAction = { action ->
            when(action){
                is VideoListScreenActions.OnSearchClick -> {
                    navController.navigate(Screen.Search.rout)
                }
                is VideoListScreenActions.OnVideoCardClick -> {
                    navController.navigate(Screen.Details.rout + "/${action.id}")
                }
                is VideoListScreenActions.OnVideoCardLongClick -> {
                }
                is VideoListScreenActions.OnFavoriteButtonClick -> {
                }
            }
        }
    )
}

@Composable
fun VideoList(
    navController: NavHostController,
    movieListState: MovieListState,
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
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 30.dp)
                        .padding(top = 50.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "What do you want to watch today?",
                        style = TextStyle(
                            fontSize = 30.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        ),
                        modifier = Modifier.weight(.7f),
                    )
                    Spacer(modifier = Modifier.width(15.dp))
                    Image(
                        modifier = Modifier
                            .size(40.dp)
                            .border(2.dp, Color.White, RoundedCornerShape(50))
                            .padding(3.dp),
                        imageVector = ImageVector.vectorResource(id = R.drawable.ic_person),
                        contentDescription = ""
                    )
                }
            }

            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 20.dp, horizontal = 30.dp)
                        .clip(RoundedCornerShape(50))
//                    .border(2.dp, Color(0x8FFFFFFF), RoundedCornerShape(50))
                        .background(Color(0xFF36076B))
                        .clickable {
                            onUserAction(VideoListScreenActions.OnSearchClick)
                        },
                    horizontalArrangement = Arrangement.SpaceBetween,
                ){
                    Text(
                        "Search...",
                        color = Color(0x8FFFFFFF),
                        modifier = Modifier
                            .padding(15.dp)
                    )
                    Icon(
                        modifier = Modifier
                            .padding(15.dp),
                        imageVector = ImageVector.vectorResource(id = R.drawable.ic_search),
                        contentDescription = "Search",
                        tint = Color(0x8FFFFFFF,)
                    )
                }
            }

            item {
                CategorySelector(
                    navController,
                    categories = listOf("Movies", "Series", "Novels", "Documentaries"),
                    onCategorySelected = {
                        selectedCategory = it
                    }
                )
            }
        }
    }
}

@Composable
fun MovieVideoList(
    videoType: String,
    movieListState: MovieListState,
    navController: NavHostController,
    onEvent: (VideoListUiEvent) -> Unit,
    lazyListState: LazyListState,
    onToggleFavorite: (Int) -> Unit
) {
    // Determine the appropriate movie list based on the video type
    val movieList = when (videoType) {
        MovieCategory.NOW_PLAYING -> movieListState.nowPlayingMovieList
        MovieCategory.POPULAR -> movieListState.popularMovieList
        MovieCategory.TOP_RATED -> movieListState.topRatedMovieList
        MovieCategory.UPCOMING -> movieListState.upcomingMovieList
        else -> emptyList() // Fallback for unsupported types
    }

    if (movieList.isEmpty() && movieListState.isLoading) {
        // Show loading indicator when the list is empty and loading
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    } else {
        // Display the video type title
        Text(
            text = videoType.toTitleCase(),
            style = TextStyle(fontSize = 18.sp, fontWeight = FontWeight.Bold),
            modifier = Modifier
                .padding(horizontal = 30.dp)
                .padding(top = 30.dp, bottom = 20.dp),
            color = Color.White
        )
        // Display the list of videos
        LazyRow(
            state = lazyListState,
            horizontalArrangement = Arrangement.spacedBy((-48).dp)
        ) {
            items(movieList.size) { index ->
                Spacer(modifier = Modifier.padding(start = 30.dp))
                VideoCard(
                    navController = navController,
                    width = 150.dp,
                    height = 230.dp,
                    poster = movieList[index].poster_path,
                    id = movieList[index].id,
                    title = movieList[index].title,
                    isFavorite = movieList[index].isFavorite,
                    route = Screen.Details.rout,
                    onToggleFavorite = onToggleFavorite,
                    onVideoCardClick = {}
                )
                Spacer(modifier = Modifier.padding(end = 30.dp))

                // Trigger pagination when nearing the end of the list
                if (index >= movieList.size - 1 && !movieListState.isLoading) {
                    onEvent(VideoListUiEvent.Paginate(videoType.lowercase()))
                }
            }
        }
    }
}

@Composable
fun SeriesVideoList(
    videoType: String,
    seriesListState: SeriesListState,
    navController: NavHostController,
    onEvent: (VideoListUiEvent) -> Unit,
    lazyListState: LazyListState,
    onToggleFavorite: (Int) -> Unit
) {
    // Determine the appropriate movie list based on the video type
    val seriesList = when (videoType) {
        SeriesCategory.AIRING_TODAY -> seriesListState.airingTodaySeriesList
        SeriesCategory.ON_THE_AIR -> seriesListState.onTheAirSeriesList
        SeriesCategory.POPULAR -> seriesListState.popularSeriesList
        SeriesCategory.TOP_RATED -> seriesListState.topRatedSeriesList
        else -> emptyList() // Fallback for unsupported types
    }

    if (seriesList.isEmpty() && seriesListState.isLoading) {
        // Show loading indicator when the list is empty and loading
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    } else {
        // Display the video type title
        Text(
            text = videoType.toTitleCase(),
            style = TextStyle(fontSize = 18.sp, fontWeight = FontWeight.Bold),
            modifier = Modifier
                .padding(horizontal = 30.dp)
                .padding(top = 30.dp, bottom = 20.dp),
            color = Color.White
        )
        // Display the list of videos
        LazyRow(
            state = lazyListState,
            horizontalArrangement = Arrangement.spacedBy((-48).dp)
        ) {
            items(seriesList.size) { index ->
                Spacer(modifier = Modifier.padding(start = 30.dp))
                VideoCard(
                    navController = navController,
                    width = 150.dp,
                    height = 230.dp,
                    poster = seriesList[index].posterPath,
                    id = seriesList[index].id,
                    title = seriesList[index].name,
                    isFavorite = seriesList[index].isFavorite,
                    route = Screen.SeriesDetails.rout,
                    onToggleFavorite = onToggleFavorite,
                    onVideoCardClick = {}
                )
                Spacer(modifier = Modifier.padding(end = 30.dp))

                // Trigger pagination when nearing the end of the list
                if (index >= seriesList.size - 1 && !seriesListState.isLoading) {
                    onEvent(VideoListUiEvent.Paginate(videoType.lowercase()))
                }
            }
        }
    }
}


@Composable
fun CategoryItem(category: String, isSelected: Boolean, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .padding(vertical = 10.dp)
            .clip(RoundedCornerShape(50.dp))
            .background(
                if (isSelected) Color(0xFFFF1F8A)
                else Color.Transparent
            )
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Text(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp),
            text = category,
            color = if (isSelected) Color.White else Color(0x8FFFFFFF),
            fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal,
            fontSize = 16.sp
        )
    }
}

@Composable
fun CategorySelector(
    navController: NavHostController,
    categories: List<String>,
    onCategorySelected: (String) -> Unit
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
                        navController,
                        nowPlayingListState = nowPlayingMoviesListState,
                        popularListState = popularMoviesListState,
                        topRatedListState = topRatedMoviesListState,
                        upcomingListState = upcomingMoviesListState
                    )
                }

                "Series" -> {
                    SeriesTab(
                        navController,
                        airingTodayListState = airingTodaySeriesListState,
                        onTheAirListState = onTheAirSeriesListState,
                        popularListState = popularSeriesListState,
                        topRatedListState = topRatedSeriesListState
                    )
                }
            }
        }
    }
}

@Composable
private fun MovieTab(
    navController: NavHostController,
    nowPlayingListState: LazyListState,
    popularListState: LazyListState,
    topRatedListState: LazyListState,
    upcomingListState: LazyListState
) {
    val movieListViewModel = hiltViewModel<MovieListViewModel>()
    val movieListState = movieListViewModel.movieListState.collectAsState().value
//    val nowPlayingListState = rememberLazyListState()
//    val popularListState = rememberLazyListState()
//    val topRatedListState = rememberLazyListState()
//    val upcomingListState = rememberLazyListState()
    Column {
        MovieVideoList(
            MovieCategory.NOW_PLAYING,
            movieListState = movieListState,
            navController = navController,
            onEvent = movieListViewModel::onEvent,
            lazyListState = nowPlayingListState,
            onToggleFavorite = {
                movieListViewModel.toggleFavorite(it)
            }
        )
        MovieVideoList(
            MovieCategory.POPULAR,
            movieListState = movieListState,
            navController = navController,
            onEvent = movieListViewModel::onEvent,
            lazyListState = popularListState,
            onToggleFavorite = {
                movieListViewModel.toggleFavorite(it)
            }
        )
        MovieVideoList(
            MovieCategory.TOP_RATED,
            movieListState = movieListState,
            navController = navController,
            onEvent = movieListViewModel::onEvent,
            lazyListState = topRatedListState,
            onToggleFavorite = {
                movieListViewModel.toggleFavorite(it)
            }
        )
        MovieVideoList(
            MovieCategory.UPCOMING,
            movieListState = movieListState,
            navController = navController,
            onEvent = movieListViewModel::onEvent,
            lazyListState = upcomingListState,
            onToggleFavorite = {
                movieListViewModel.toggleFavorite(it)
            }
        )
    }
}

@Composable
private fun SeriesTab(
    navController: NavHostController,
    airingTodayListState: LazyListState,
    onTheAirListState: LazyListState,
    popularListState: LazyListState,
    topRatedListState: LazyListState
) {
    val seriesViewModel = hiltViewModel<SeriesViewModel>()
    val seriesListState = seriesViewModel.seriesListState.collectAsState().value

    Column {
        SeriesVideoList(
            SeriesCategory.AIRING_TODAY,
            seriesListState = seriesListState,
            navController = navController,
            onEvent = seriesViewModel::onEvent,
            lazyListState = airingTodayListState,
            onToggleFavorite = {
                seriesViewModel.toggleFavorite(it)
            }
        )
        SeriesVideoList(
            SeriesCategory.ON_THE_AIR,
            seriesListState = seriesListState,
            navController = navController,
            onEvent = seriesViewModel::onEvent,
            lazyListState = onTheAirListState,
            onToggleFavorite = {
                seriesViewModel.toggleFavorite(it)
            }
        )
        SeriesVideoList(
            SeriesCategory.POPULAR,
            seriesListState = seriesListState,
            navController = navController,
            onEvent = seriesViewModel::onEvent,
            lazyListState = popularListState,
            onToggleFavorite = {
                seriesViewModel.toggleFavorite(it)
            }
        )
        SeriesVideoList(
            SeriesCategory.TOP_RATED,
            seriesListState = seriesListState,
            navController = navController,
            onEvent = seriesViewModel::onEvent,
            lazyListState = topRatedListState,
            onToggleFavorite = {
                seriesViewModel.toggleFavorite(it)
            }
        )
    }
}

sealed class VideoListScreenActions{
    data object OnSearchClick: VideoListScreenActions()
    data class OnVideoCardClick(val id: Int): VideoListScreenActions()
    data class OnVideoCardLongClick(val id: Int): VideoListScreenActions()
    data class OnFavoriteButtonClick(val id: Int): VideoListScreenActions()
}
