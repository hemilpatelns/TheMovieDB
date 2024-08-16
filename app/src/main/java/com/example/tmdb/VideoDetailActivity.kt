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
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
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

class VideoDetailActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            VideoDetails()
        }
    }
}

@Preview
@Composable
private fun VideoDetailsPreview() {
    VideoDetails()
}

@Composable
fun VideoDetails() {
    var isFavorite by remember {
        mutableStateOf(false)
    }
    val context = LocalContext.current
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
            Image(
                painter = painterResource(id = R.drawable.img_home),
                contentDescription = "Video Image",
                modifier = Modifier
                    .constrainAs(videoImage) {}
                    .fillMaxWidth()
                    .height(450.dp),
                contentScale = ContentScale.FillWidth
            )
            Spacer(modifier = Modifier
                .fillMaxWidth()
                .height(150.dp)
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color.Transparent,
                            Color(0x7F15151D),
                            Color(0xFF15151D)
                        )
                    )
                )
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
                        CircularProgressIndicator(
                            modifier = Modifier
                                .constrainAs(ratingGraphic) {}
                                .size(60.dp),
                            progress = 0.79f,
                            color = Color(0xFFFF1F8A),
                            trackColor = Color(0xFF303243),
                            strokeCap = StrokeCap.Round,
                            strokeWidth = 6.dp
                        )
                        Text(text = "79%",
                            color = Color.White,
                            style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 16.sp),
                            modifier = Modifier.constrainAs(ratingValue) {
                                start.linkTo(ratingGraphic.start)
                                end.linkTo(ratingGraphic.end)
                                top.linkTo(ratingGraphic.top)
                                bottom.linkTo(ratingGraphic.bottom)
                            })
                    }
                    Spacer(modifier = Modifier.padding(16.dp))
                    Column {
                        Text(
                            modifier = Modifier.padding(bottom = 7.dp),
                            text = "Star Wars: Episode III - Revenge of the Sith",
                            color = Color.White,
                            style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 24.sp),
                        )
                        Row {
                            Icon(
                                imageVector = ImageVector.vectorResource(id = R.drawable.ic_duration),
                                contentDescription = "Duration icon",
                                tint = Color(0xFFBBBBBB),
                                modifier = Modifier
                                    .align(alignment = Alignment.CenterVertically)
                                    .padding(end = 7.dp)
                            )
                            Text(
                                text = "2h 20m",
                                color = Color(0xFFBBBBBB),
                                style = TextStyle(fontSize = 16.sp)
                            )
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
        Text(
            text = "Anakin joins forces with Obi-Wan and sets Palpatine fre from the clutches of Count Dooku. However, he falls prey to Palpatine and the Jedi's mind games and gives into temptation",
            color = Color(0xFFCCCCCC),
            modifier = Modifier.padding(horizontal = 30.dp),
            style = TextStyle(fontSize = 14.sp)
        )
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

        VideoList(videoType = "Recommendations", videoName = "Recommended")

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
                activity?.finish()
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

