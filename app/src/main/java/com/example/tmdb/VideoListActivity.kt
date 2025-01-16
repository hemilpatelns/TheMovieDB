package com.example.tmdb

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
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Warning
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import coil.size.Size
import com.example.tmdb.movieList.data.remote.CommonApi
import com.example.tmdb.movieList.domain.model.Movie
import com.example.tmdb.movieList.domain.model.Series
import com.example.tmdb.movieList.presentation.MovieListState
import com.example.tmdb.movieList.presentation.VideoListUiEvent
import com.example.tmdb.movieList.presentation.MovieListViewModel
import com.example.tmdb.movieList.presentation.SeriesListState
import com.example.tmdb.movieList.presentation.SeriesViewModel
import com.example.tmdb.movieList.util.MovieCategory
import com.example.tmdb.movieList.util.FunctionUtil
import com.example.tmdb.movieList.util.Screen
import com.example.tmdb.movieList.util.SeriesCategory
import com.example.tmdb.movieList.util.toTitleCase
import com.example.tmdb.ui.theme.gradientBrushOne

@Composable
fun VideoList(
    navController: NavHostController,
) {
    val isEdgeToEdge = FunctionUtil.isEdgeToEdgeEnabled(LocalView.current)
    val movieListViewModel = hiltViewModel<MovieListViewModel>()
    val movieListState = movieListViewModel.movieListState.collectAsState().value
    var selectedCategory by remember {
        mutableStateOf("Movies")
    }
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(gradientBrushOne),
        contentPadding = PaddingValues(bottom = if (isEdgeToEdge) 40.dp else 0.dp)
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
            SearchScreen()
        }

        item {
            CategorySelector(
                navController,
                categories = listOf("Movies", "Series", "Anime", "Novels", "Documentaries"),
                onCategorySelected = {
                    selectedCategory = it
                }
            )
        }
    }
}

@Composable
fun SearchBar(
    query: String,
    onQueryChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 20.dp, horizontal = 30.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // TextField for input
        TextField(
            value = query,
            onValueChange = { onQueryChange(it) },
            placeholder = { Text("Search", color = Color(0x8FFFFFFF)) },
            singleLine = true,
            modifier = Modifier
                .weight(1f),
            trailingIcon = {
                Icon(
                    imageVector = ImageVector.vectorResource(id = R.drawable.ic_search), // Replace with your search icon
                    contentDescription = "Search",
                    tint = Color(0x8FFFFFFF)
                )
            },
            shape = RoundedCornerShape(50.dp),
            colors = TextFieldDefaults.colors(
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White,
                focusedContainerColor = Color(0xFF36076B),
                unfocusedContainerColor = Color(0xFF36076B),
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            )
        )
    }
}

@Composable
fun SearchScreen() {
    var searchQuery by remember { mutableStateOf("") }

    Column {
        SearchBar(
            query = searchQuery,
            onQueryChange = { searchQuery = it }
        )
    }
}

@Composable
fun VideoCard(movie: Movie, navController: NavHostController) {
    val context = LocalContext.current
    val imageState = rememberAsyncImagePainter(
        model = ImageRequest.Builder(context)
            .data(CommonApi.IMAGE_BASE_URL + movie.poster_path)
            .size(Size.ORIGINAL)
            .build()
    ).state

    Box(
        contentAlignment = Alignment.BottomCenter,
        modifier = Modifier
            .background(color = Color.Transparent)
            .width(150.dp)
            .height(230.dp)
            .clip(RoundedCornerShape(10))
            .clickable {
                navController.navigate(Screen.Details.rout + "/${movie.id}")
            },
    ) {
        if (imageState is AsyncImagePainter.State.Error) {
            Box(
                modifier = Modifier
                    .width(150.dp)
                    .height(230.dp)
                    .aspectRatio(.65f)
                    .clip(RoundedCornerShape(10)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Rounded.Warning,
                    contentDescription = movie.title
                )
            }
        }

        if (imageState is AsyncImagePainter.State.Success) {
            Image(
                modifier = Modifier
                    .width(150.dp)
                    .height(230.dp)
                    .aspectRatio(.65f)
                    .clip(RoundedCornerShape(10)),
                painter = imageState.painter,
                contentDescription = movie.title,
                contentScale = ContentScale.Crop
            )
        }
        if (imageState is AsyncImagePainter.State.Loading) {
            Text(
                text = movie.title,
                color = Color.White,
                style = TextStyle(fontWeight = FontWeight.SemiBold),
                overflow = TextOverflow.Ellipsis,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 5.dp, vertical = 8.dp)
            )
        }
    }
}

@Composable
fun SeriesVideoCard(series: Series, navController: NavHostController) {
    val context = LocalContext.current
    val imageState = rememberAsyncImagePainter(
        model = ImageRequest.Builder(context)
            .data(CommonApi.IMAGE_BASE_URL + series.posterPath)
            .size(Size.ORIGINAL)
            .build()
    ).state

    Box(
        contentAlignment = Alignment.BottomCenter,
        modifier = Modifier
            .background(color = Color.Transparent)
            .width(150.dp)
            .height(230.dp)
            .clip(RoundedCornerShape(10))
//            .clickable {
//                navController.navigate(Screen.Details.rout + "/${movie.id}")
//            },
    ) {
        if (imageState is AsyncImagePainter.State.Error) {
            Box(
                modifier = Modifier
                    .width(150.dp)
                    .height(230.dp)
                    .aspectRatio(.65f)
                    .clip(RoundedCornerShape(10)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Rounded.Warning,
                    contentDescription = series.name
                )
            }
        }

        if (imageState is AsyncImagePainter.State.Success) {
            Image(
                modifier = Modifier
                    .width(150.dp)
                    .height(230.dp)
                    .aspectRatio(.65f)
                    .clip(RoundedCornerShape(10)),
                painter = imageState.painter,
                contentDescription = series.name,
                contentScale = ContentScale.Crop
            )
        }
        if (imageState is AsyncImagePainter.State.Loading) {
            Text(
                text = series.name,
                color = Color.White,
                style = TextStyle(fontWeight = FontWeight.SemiBold),
                overflow = TextOverflow.Ellipsis,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 5.dp, vertical = 8.dp)
            )
        }
    }
}


@Composable
fun MovieVideoList(
    videoType: String,
    movieListState: MovieListState,
    navController: NavHostController,
    onEvent: (VideoListUiEvent) -> Unit,
    lazyListState: LazyListState
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
                    movie = movieList[index],
                    navController = navController
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
    lazyListState: LazyListState
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
                SeriesVideoCard(
                    series = seriesList[index],
                    navController = navController
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
    var selectedCategory by remember { mutableStateOf(categories.first()) }
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
            lazyListState = nowPlayingListState
        )
        MovieVideoList(
            MovieCategory.POPULAR,
            movieListState = movieListState,
            navController = navController,
            onEvent = movieListViewModel::onEvent,
            lazyListState = popularListState
        )
        MovieVideoList(
            MovieCategory.TOP_RATED,
            movieListState = movieListState,
            navController = navController,
            onEvent = movieListViewModel::onEvent,
            lazyListState = topRatedListState
        )
        MovieVideoList(
            MovieCategory.UPCOMING,
            movieListState = movieListState,
            navController = navController,
            onEvent = movieListViewModel::onEvent,
            lazyListState = upcomingListState
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
            lazyListState = airingTodayListState
        )
        SeriesVideoList(
            SeriesCategory.ON_THE_AIR,
            seriesListState = seriesListState,
            navController = navController,
            onEvent = seriesViewModel::onEvent,
            lazyListState = onTheAirListState
        )
        SeriesVideoList(
            SeriesCategory.POPULAR,
            seriesListState = seriesListState,
            navController = navController,
            onEvent = seriesViewModel::onEvent,
            lazyListState = popularListState
        )
        SeriesVideoList(
            SeriesCategory.TOP_RATED,
            seriesListState = seriesListState,
            navController = navController,
            onEvent = seriesViewModel::onEvent,
            lazyListState = topRatedListState
        )
    }
}
