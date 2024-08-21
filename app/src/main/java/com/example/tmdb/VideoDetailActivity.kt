package com.example.tmdb

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavHostController
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import coil.size.Size
import com.example.tmdb.details.presentation.MovieDetailsViewModel
import com.example.tmdb.movieList.data.remote.MovieApi
import com.example.tmdb.ui.theme.gradientBrushTwo

@Composable
fun VideoDetails(navController: NavHostController) {
    val context = LocalContext.current
    val movieDetailsViewModel = hiltViewModel<MovieDetailsViewModel>()
    val movieDetailsState = movieDetailsViewModel.movieDetailsState.collectAsState().value

    val backDropImageState = rememberAsyncImagePainter(
        model = ImageRequest.Builder(context)
            .data(MovieApi.IMAGE_BASE_URL + movieDetailsState.movie?.backdrop_path)
            .size(Size.ORIGINAL)
            .build()
    ).state
    var isFavorite by remember {
        mutableStateOf(false)
    }
    val activity = context as? ComponentActivity
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .background(
                color = Color(0xFF15151D),
            )
    ) {

        ConstraintLayout {
            val (videoImage, videoRatings, blurBg) = createRefs()
            if (backDropImageState is AsyncImagePainter.State.Error) {
                Box(
                    modifier = Modifier
                        .constrainAs(videoImage) {}
                        .fillMaxWidth()
                        .height(450.dp),
                ) {
                    Icon(
                        modifier = Modifier.size(70.dp),
                        imageVector = Icons.Rounded.Warning,
                        contentDescription = movieDetailsState.movie?.title
                    )
                }
            }

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
//            Image(
//                painter = painterResource(id = R.drawable.img_home),
//                contentDescription = "Video Image",
//                modifier = Modifier
//                    .constrainAs(videoImage) {}
//                    .fillMaxWidth()
//                    .height(450.dp),
//                contentScale = ContentScale.FillWidth
//            )
            Spacer(modifier = Modifier
                .fillMaxWidth()
                .height(150.dp)
                .background(gradientBrushTwo)
                .constrainAs(blurBg) {
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    bottom.linkTo(videoImage.bottom)
                })
            Box(
                modifier = Modifier.constrainAs(videoRatings) {
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    top.linkTo(videoImage.bottom)
                    bottom.linkTo(videoImage.bottom)
                }
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 30.dp),
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
                            Text(text = "${(movie.vote_average * 10).toInt()}%",
                                color = Color.White,
                                style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 16.sp),
                                modifier = Modifier.constrainAs(ratingValue) {
                                    start.linkTo(ratingGraphic.start)
                                    end.linkTo(ratingGraphic.end)
                                    top.linkTo(ratingGraphic.top)
                                    bottom.linkTo(ratingGraphic.bottom)
                                })
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
//                                imageVector = ImageVector.vectorResource(id = R.drawable.ic_duration),
                                imageVector = Icons.Outlined.DateRange,
                                contentDescription = movieDetailsState.movie?.release_date,
                                tint = Color(0xFFBBBBBB),
                                modifier = Modifier
//                                    .align(alignment = Alignment.CenterVertically)
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
        Spacer(
            modifier = Modifier
                .padding(horizontal = 30.dp, vertical = 30.dp)
                .fillMaxWidth()
                .height(1.dp)
                .background(color = Color(0x7A000000))
        )
        movieDetailsState.movie?.let { movie ->
            Text(
                text = movie.overview,
                color = Color(0xFFCCCCCC),
                modifier = Modifier.padding(horizontal = 30.dp),
                style = TextStyle(fontSize = 14.sp)
            )
        }
        Button(
            onClick = { },
            modifier = Modifier
                .padding(
                    horizontal = 30.dp,
                    vertical = 30.dp
                )
                .height(48.dp)
                .fillMaxWidth()
                .align(Alignment.CenterHorizontally)
                .background(
                    brush = Brush.linearGradient(
                        colors = listOf(Color(0xFF8000FF), Color(0xFF4D0099)),
                    ),
                    shape = RoundedCornerShape(30.dp)
                ),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Transparent, // Makes the button background transparent
                contentColor = Color.White // Sets text color
            )
        ) {
            Icon(
                imageVector = ImageVector.vectorResource(id = R.drawable.ic_play_circle_outline),
                contentDescription = "Play button",
                modifier = Modifier.padding(horizontal = 5.dp)
            )
            Text(
                text = "Watch Trailer",
                style = TextStyle(
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold
                ),
                modifier = Modifier.padding(horizontal = 5.dp)
            )
        }
        Text(
            text = "Main Cast",
            color = Color.White,
            style = TextStyle(fontSize = 18.sp, fontWeight = FontWeight.Bold),
            modifier = Modifier
                .padding(top = 16.dp, bottom = 20.dp)
                .padding(horizontal = 30.dp)
        )
        LazyRow(horizontalArrangement = Arrangement.spacedBy((-50).dp)) {
            items(10) {
                Spacer(modifier = Modifier.width(30.dp))
                CastCard("Artist $it")
                Spacer(modifier = Modifier.width(30.dp))
            }
        }

        Text(
            text = "Category(s)",
            color = Color.White,
            style = TextStyle(
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            ),
            modifier = Modifier
                .padding(horizontal = 30.dp)
                .padding(top = 40.dp)
        )

        DisplayGenre(genreList = listOf("Drama", "Thriller"))

//        VideoList(videoType = "Recommendations", videoName = "Recommended")

        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .height(30.dp)
        )
    }
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 50.dp, end = 20.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = {
                navController.popBackStack()
            }) {
                Icon(
                    tint = Color.White,
                    imageVector = Icons.Filled.ArrowBack,
                    contentDescription = "Back Button"
                )
            }
            Text(
                text = "Back",
                color = Color.White,
                style = TextStyle(
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                ),
            )
        }
        IconButton(
            onClick = {
                isFavorite = !isFavorite
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
fun CastCard(artistName: String) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            modifier = Modifier
                .size(70.dp)
                .border(
                    4.dp, Color(0xFF303243),
                    RoundedCornerShape(50)
                ),
            painter = painterResource(id = R.drawable.ic_person),
            contentDescription = "Cast Image"
        )
        Text(
            modifier = Modifier.padding(vertical = 10.dp),
            text = artistName,
            color = Color.White,
            style = TextStyle(
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold
            )
        )
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

