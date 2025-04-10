package com.example.tmdb.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Warning
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import coil.size.Size
import com.example.tmdb.data.remote.CommonApi
import com.example.tmdb.domain.model.SearchData
import com.example.tmdb.domain.util.Screen
import com.example.tmdb.domain.util.shimmerEffect

@Composable
fun CommonVideoCard(
    video: SearchData,
    navController: NavHostController,
    width: Dp,
    height: Dp
) {
    val context = LocalContext.current
    val imageState = rememberAsyncImagePainter(
        model = ImageRequest.Builder(context)
            .data(CommonApi.IMAGE_BASE_URL + video.posterPath)
            .size(Size.ORIGINAL)
            .build()
    ).state

    Box(
        contentAlignment = Alignment.BottomCenter,
        modifier = Modifier
            .width(width)
            .height(height)
            .clip(RoundedCornerShape(10))
            .clickable {
                when(video.mediaType){
                    "movie" -> {
                        navController.navigate(Screen.Details.rout + "/${video.id}")
                    }
                    "tv" -> {
                        navController.navigate(Screen.SeriesDetails.rout + "/${video.id}")
                    }
                }
            },
    ) {
        if (imageState is AsyncImagePainter.State.Error) {
            Box(
                modifier = Modifier
                    .width(width)
                    .height(height)
                    .background(Color(0x8FFFFFFF))
                    .clip(RoundedCornerShape(10)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Rounded.Warning,
                    contentDescription = video.formattedTitle
                )
                Text(
                    text = video.formattedTitle.orEmpty(),
                    textAlign = TextAlign.Center,
                    color = Color.White,
                    style = TextStyle(fontWeight = FontWeight.SemiBold),
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier
                        .padding(horizontal = 5.dp, vertical = 8.dp)
                        .align(Alignment.BottomCenter)
                )
            }
        }

        if (imageState is AsyncImagePainter.State.Success) {
            Image(
                modifier = Modifier
                    .width(width)
                    .height(height)
                    .clip(RoundedCornerShape(10)),
                painter = imageState.painter,
                contentDescription = video.formattedTitle,
                contentScale = ContentScale.Crop
            )
        }
        if (imageState is AsyncImagePainter.State.Loading) {
            Box(
                modifier = Modifier
                    .width(width)
                    .height(height)
                    .clip(RoundedCornerShape(10))
                    .shimmerEffect(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = video.formattedTitle.orEmpty(),
                    color = Color.White,
                    style = TextStyle(fontWeight = FontWeight.SemiBold),
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier
                        .padding(horizontal = 5.dp, vertical = 8.dp)
                        .align(Alignment.BottomCenter)
                )
            }
        }
    }
}