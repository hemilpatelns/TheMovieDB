package com.example.tmdb.presentation.list.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.tmdb.domain.util.MovieCategory
import com.example.tmdb.domain.util.Screen
import com.example.tmdb.domain.util.toTitleCase
import com.example.tmdb.presentation.components.VideoCard
import com.example.tmdb.presentation.list.MovieListState
import com.example.tmdb.presentation.list.VideoListScreenActions

@Composable
fun MovieVideoList(
    videoType: String,
    movieListState: MovieListState,
    navController: NavHostController,
    lazyListState: LazyListState,
    onToggleVideoCardUi: (String, Int) -> Unit,
    onToggleFavorite: (Int, String) -> Unit,
    onListEnd: (String, String) -> Unit,
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
                    showLongPressUi = movieList[index].showLongClickUi,
                    isFavorite = movieList[index].isFavorite,
                    route = Screen.Details.rout,
                    onToggleVideoCardUi = { id ->
                        onToggleVideoCardUi("movie", id)
                    },
                    onToggleFavorite = { id ->
                        onToggleFavorite(id, "movie")
                    },
                    onVideoCardClick = {}
                )
                Spacer(modifier = Modifier.padding(end = 30.dp))

                // Trigger pagination when nearing the end of the list
                if (index >= movieList.size - 1 && !movieListState.isLoading) {
                    onListEnd(videoType.lowercase(), "movie")
                }
            }
        }
    }
}