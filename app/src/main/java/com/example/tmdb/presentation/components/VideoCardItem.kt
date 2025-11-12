package com.example.tmdb.presentation.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.rounded.Warning
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import coil.size.Size
import com.example.tmdb.data.remote.CommonApi
import com.example.tmdb.domain.util.Screen
import com.example.tmdb.domain.util.shimmerEffect

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun VideoCard(
    navController: NavHostController,
    width: Dp,
    height: Dp,
    poster: String,
    id: Int,
    title: String,
    isFavorite: Boolean,
    route: String,
    onToggleFavorite: (Int) -> Unit,
    onVideoCardClick: () -> Unit
) {
    val context = LocalContext.current
    val haptics = LocalHapticFeedback.current
    var showLongPressUi by remember { mutableStateOf(false) }
    val imageState = rememberAsyncImagePainter(
        model = ImageRequest.Builder(context)
            .data(CommonApi.IMAGE_BASE_URL + poster)
            .size(Size.ORIGINAL)
            .build()
    ).state

    Box(
        contentAlignment = Alignment.BottomCenter,
        modifier = Modifier
            .width(width)
            .height(height)
            .clip(RoundedCornerShape(10))
            .combinedClickable(
                onClick = {
                    if (!showLongPressUi) {
                        navController.navigate(route + "/${id}")
                    }
                    showLongPressUi = false
                },
                onLongClick = {
                    haptics.performHapticFeedback(HapticFeedbackType.LongPress)
                    showLongPressUi = true
                }
            ),
    ) {
        if (imageState is AsyncImagePainter.State.Error) {
            Box(
                modifier = Modifier
                    .width(width)
                    .height(height)
                    .clip(RoundedCornerShape(10))
                    .shimmerEffect(),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Rounded.Warning,
                    contentDescription = title
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
                contentDescription = title,
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
                    text = title,
                    color = Color.White,
                    style = TextStyle(fontWeight = FontWeight.SemiBold),
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier
                        .padding(horizontal = 5.dp, vertical = 8.dp)
                        .align(Alignment.BottomCenter)
                )
            }
        }

        if (showLongPressUi) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        color = Color.Black.copy(alpha = 0.9f),
                    ),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    modifier = Modifier
                        .weight(0.5f)
                        .padding(horizontal = 5.dp, vertical = 8.dp),
                    text = title,
                    textAlign = TextAlign.Center,
                    color = Color.White,
                    fontSize = 16.sp,
                    style = TextStyle(fontWeight = FontWeight.SemiBold),
                    overflow = TextOverflow.Ellipsis
                )
                IconButton(
                    modifier = Modifier
                        .weight(0.5f)
                        .size(40.dp),
                    onClick = {
                        onToggleFavorite(id)

                    }
                ) {
                    Icon(
                        modifier = Modifier
                            .fillMaxSize(),
                        imageVector = if (isFavorite) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                        contentDescription = if (isFavorite) "Favorite" else "Not Favorite",
                        tint = if (isFavorite) Color(0xFFFF1F8A) else Color.White
                    )
                }
            }
        }
    }
}