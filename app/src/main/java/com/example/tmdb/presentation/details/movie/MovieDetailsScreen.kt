package com.example.tmdb.presentation.details.movie

import android.content.Context
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.DateRange
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.rounded.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import coil.size.Size
import com.example.tmdb.R
import com.example.tmdb.data.remote.CommonApi
import com.example.tmdb.data.remote.respond.Cast
import com.example.tmdb.domain.util.FunctionUtil
import com.example.tmdb.ui.theme.gradientBrushTwo

@Composable
fun VideoDetails(navController: NavHostController) {
    val context = LocalContext.current
    val isEdgeToEdge = FunctionUtil.isEdgeToEdgeEnabled(LocalView.current)
    val movieDetailsViewModel = hiltViewModel<MovieDetailsViewModel>()
    val movieDetailsState = movieDetailsViewModel.movieDetailsState.collectAsState().value
    val movie = movieDetailsState.movie
    val castList = movieDetailsState.movie?.cast
    val isFavorite = movieDetailsState.isFavorite

    val backDropImageState = rememberAsyncImagePainter(
        model = ImageRequest.Builder(context)
            .data(CommonApi.IMAGE_BASE_URL + movie?.backdrop_path)
            .size(Size.ORIGINAL)
            .build()
    ).state
//    val activity = context as? ComponentActivity
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color(0xFF15151D)),
        contentPadding = PaddingValues(bottom = if (isEdgeToEdge) 40.dp else 0.dp)
    ) {
        item {
            ConstraintLayout {
                val (videoImage, videoRatings, blurBg) = createRefs()

                // Error state for image
                if (backDropImageState is AsyncImagePainter.State.Error) {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .constrainAs(videoImage) {}
                            .fillMaxWidth()
                            .height(450.dp),
                    ) {
                        Icon(
                            modifier = Modifier.size(200.dp),
                            imageVector = Icons.Rounded.Warning,
                            tint = Color(0xFFBBBBBB),
                            contentDescription = movieDetailsState.movie?.title
                        )
                    }
                }

                // Success state for image
                if (backDropImageState is AsyncImagePainter.State.Success) {
                    Image(
                        painter = backDropImageState.painter,
                        contentDescription = movieDetailsState.movie?.title,
                        modifier = Modifier
                            .constrainAs(videoImage) {}
                            .fillMaxWidth()
                            .height(450.dp),
                        contentScale = ContentScale.Crop
                    )
                }

                Spacer(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(150.dp)
                        .background(gradientBrushTwo)
                        .constrainAs(blurBg) {
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                            bottom.linkTo(videoImage.bottom)
                        }
                )

                Box(
                    modifier = Modifier.constrainAs(videoRatings) {
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                        top.linkTo(videoImage.bottom)
                        bottom.linkTo(videoImage.bottom)
                    }
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 20.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        ConstraintLayout {
                            val (ratingGraphic, ratingBg, ratingValue) = createRefs()

                            Box(
                                modifier = Modifier
                                    .constrainAs(ratingBg) {
                                        start.linkTo(ratingGraphic.start)
                                        end.linkTo(ratingGraphic.end)
                                        top.linkTo(ratingGraphic.top)
                                        bottom.linkTo(ratingGraphic.bottom)
                                    }
                                    .size(50.dp)
                                    .background(
                                        color = Color(0xFF15161D),
                                        shape = RoundedCornerShape(50)
                                    )
                            )

                            movieDetailsState.movie?.let { movie ->
                                CircularProgressIndicator(
                                    modifier = Modifier
                                        .constrainAs(ratingGraphic) {}
                                        .size(60.dp),
                                    progress = ((movie.vote_average) / 10).toFloat(),
                                    color = Color(0xFFFF1F8A),
                                    trackColor = Color(0xFF303243),
                                    strokeCap = StrokeCap.Round,
                                    strokeWidth = 6.dp
                                )

                                Text(
                                    text = "${(movie.vote_average * 10).toInt()}%",
                                    color = Color.White,
                                    style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 16.sp),
                                    modifier = Modifier.constrainAs(ratingValue) {
                                        start.linkTo(ratingGraphic.start)
                                        end.linkTo(ratingGraphic.end)
                                        top.linkTo(ratingGraphic.top)
                                        bottom.linkTo(ratingGraphic.bottom)
                                    }
                                )
                            }
                        }

                        Spacer(modifier = Modifier.padding(16.dp))

                        Column {
                            movieDetailsState.movie?.let { movie ->
                                Text(
                                    modifier = Modifier.padding(bottom = 7.dp),
                                    text = movie.title,
                                    color = Color.White,
                                    style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 24.sp),
                                )
                            }

                            Row(
                                verticalAlignment = Alignment.Bottom,
                                horizontalArrangement = Arrangement.spacedBy(10.dp),
                                modifier = Modifier.padding(vertical = 8.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Outlined.DateRange,
                                    contentDescription = movieDetailsState.movie?.release_date,
                                    tint = Color(0xFFBBBBBB),
                                )
                                movieDetailsState.movie?.let { movie ->
                                    Text(
                                        text = movie.release_date,
                                        color = Color(0xFFBBBBBB),
                                        style = TextStyle(fontSize = 16.sp)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        item {
            Spacer(
                modifier = Modifier
                    .padding(horizontal = 30.dp, vertical = 30.dp)
                    .fillMaxWidth()
                    .height(1.dp)
                    .background(color = Color(0x7A000000))
            )
        }

        item {
            movieDetailsState.movie?.let { movie ->
                Text(
                    text = movie.overview,
                    color = Color(0xFFCCCCCC),
                    modifier = Modifier.padding(horizontal = 30.dp),
                    style = TextStyle(fontSize = 14.sp)
                )
            }
        }

        item {
            Spacer(modifier = Modifier.height(16.dp))
        }

        item {
            Button(
                onClick = { },
                modifier = Modifier
                    .padding(horizontal = 30.dp, vertical = 30.dp)
                    .height(48.dp)
                    .fillMaxWidth()
                    .background(
                        brush = Brush.linearGradient(
                            colors = listOf(Color(0xFF8000FF), Color(0xFF4D0099)),
                        ),
                        shape = RoundedCornerShape(30.dp)
                    ),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Transparent,
                    contentColor = Color.White
                )
            ) {
                Icon(
                    imageVector = ImageVector.vectorResource(id = R.drawable.ic_play_circle_outline),
                    contentDescription = "Play button",
                    modifier = Modifier.padding(horizontal = 5.dp)
                )
                Text(
                    text = "Watch Trailer",
                    style = TextStyle(fontSize = 16.sp, fontWeight = FontWeight.SemiBold),
                    modifier = Modifier.padding(horizontal = 5.dp)
                )
            }
        }

        item {
            Text(
                text = "Main Cast",
                color = Color.White,
                style = TextStyle(fontSize = 18.sp, fontWeight = FontWeight.Bold),
                modifier = Modifier
                    .padding(top = 16.dp, bottom = 20.dp)
                    .padding(horizontal = 30.dp)
            )
        }

        item {
            LazyRow(horizontalArrangement = Arrangement.spacedBy((-40).dp)) {
                castList?.let { list ->
                    items(list) { cast ->
                        Spacer(modifier = Modifier.width(30.dp))
                        CastCard(cast, context)
                        Spacer(modifier = Modifier.width(30.dp))
                    }
                }
            }
        }

//        item {
//            Spacer(modifier = Modifier.height(16.dp))
//        }

        item {
            Text(
                text = "Category(s)",
                color = Color.White,
                style = TextStyle(fontSize = 18.sp, fontWeight = FontWeight.Bold),
                modifier = Modifier
                    .padding(horizontal = 30.dp)
                    .padding(top = 40.dp)
            )
        }

        item {
            DisplayGenre(genreList = listOf("Drama", "Thriller"))
        }
    }
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 50.dp)
            .padding(horizontal = 20.dp)
    ) {
        IconButton(
            onClick = {
                navController.popBackStack()
            },
            modifier = Modifier
                .background(
                    color = Color(0xA6303243),
                    shape = RoundedCornerShape(50)
                )
                .size(40.dp)
        ) {
            Icon(
                tint = Color.White,
                imageVector = Icons.Filled.ArrowBack,
                contentDescription = "Back Button"
            )
        }
        IconButton(
            onClick = {
                if (movie != null) {
                    movieDetailsViewModel.toggleFavorite(movie.id)
                }
            },
            modifier = Modifier
                .background(
                    color = Color(0xA6303243),
                    shape = RoundedCornerShape(50)
                )
                .size(40.dp)
        ) {
            Icon(
                imageVector = if (isFavorite) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                contentDescription = if (isFavorite) "Favorite" else "Not Favorite",
                tint = if (isFavorite) Color(0xFFFF1F8A) else Color.White
            )
        }
    }
}

@Composable
fun CastCard(cast: Cast, context: Context) {
    Column(
        modifier = Modifier
            .width(90.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = if(cast.profile_path != ""){
                rememberAsyncImagePainter(
                    model = ImageRequest.Builder(context)
                        .data(CommonApi.IMAGE_BASE_URL + cast.profile_path)
                        .size(Size.ORIGINAL)
                        .placeholder(R.drawable.ic_person)
                        .build()
                )
            } else{
                painterResource(id = R.drawable.ic_person)
            },
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(90.dp)
                .border(
                    border = BorderStroke(3.dp, Color(0xFF303243)),
                    shape = RoundedCornerShape(50)
                )
                .padding(1.dp)
                .clip(RoundedCornerShape(50)),
            contentDescription = "Cast Image"
        )
        cast.name?.let {
            Box(modifier = Modifier
                .height(60.dp)
                .fillMaxWidth(),
                contentAlignment = Alignment.Center
            ){
                Text(
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .padding(vertical = 10.dp),
                    text = it,
                    maxLines = 3,
                    color = Color.White,
                    style = TextStyle(
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,

                    )
                )
            }
        }
    }
}

@Composable
fun GenreCard(genre: String) {
    Box(
        modifier = Modifier
            .padding(vertical = 20.dp)
            .background(
                Color(0xFF303243),
                shape = RoundedCornerShape(50.dp)
            ),
        contentAlignment = Alignment.Center
    ) {
        Text(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp),
            text = genre,
            color = Color.White,
            fontSize = 15.sp
        )
    }
}

@Composable
fun DisplayGenre(genreList: List<String>) {
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy((-46).dp),
        modifier = Modifier
            .fillMaxWidth()
    ) {
        items(genreList) { genre ->
            Spacer(modifier = Modifier.padding(start = 30.dp))
            GenreCard(genre = genre)
            Spacer(modifier = Modifier.padding(start = 30.dp))
        }
    }
}

