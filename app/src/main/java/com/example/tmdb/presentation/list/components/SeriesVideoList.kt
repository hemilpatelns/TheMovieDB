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
import com.example.tmdb.domain.util.Screen
import com.example.tmdb.domain.util.SeriesCategory
import com.example.tmdb.domain.util.toTitleCase
import com.example.tmdb.presentation.components.VideoCard
import com.example.tmdb.presentation.list.SeriesListState

@Composable
fun SeriesVideoList(
    videoType: String,
    seriesListState: SeriesListState,
    navController: NavHostController,
    lazyListState: LazyListState,
    onToggleFavorite: (Int, String) -> Unit,
    onListEnd: (String, String) -> Unit
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
                    onToggleFavorite = { id ->
                        onToggleFavorite(id, "series")
                    },
                    onVideoCardClick = {}
                )
                Spacer(modifier = Modifier.padding(end = 30.dp))

                // Trigger pagination when nearing the end of the list
                if (index >= seriesList.size - 1 && !seriesListState.isLoading) {
                    onListEnd(videoType.lowercase(), "series")
                }
            }
        }
    }
}