package com.example.tmdb

import android.content.Context
import android.content.Intent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.tmdb.movieList.util.Screen

@Composable
fun HomeScreen(navController: NavHostController) {

//    val context = LocalContext.current
    Box {
        Image(
            painter = painterResource(id = R.drawable.img_home),
            contentDescription = "",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )
        Column(
            verticalArrangement = Arrangement.Bottom,
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp)

        ) {
            Image(
                imageVector = ImageVector.vectorResource(id = R.drawable.ic_tmdb),
                contentDescription = "",
                alignment = Alignment.TopStart,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(17.dp)
            )
            Spacer(modifier = Modifier.height(15.dp))
            Text(
                text = "Everything about movies, series, anime and much more.",
                style = TextStyle(fontSize = 36.sp, color = Color.White),
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(20.dp))
            Text(
                text = "Stay up to date with information about films, series, anime and much more.",
                style = TextStyle(fontSize = 18.sp), color = Color.White
            )
            Spacer(modifier = Modifier.height(20.dp))
            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .background(
                        brush = Brush.linearGradient(
                            colors = listOf(Color(0xFF8000FF), Color(0xFF4D0099)),
                        ),
                        shape = RoundedCornerShape(30.dp)
                    ),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Transparent, // Makes the button background transparent
                    contentColor = Color.White // Sets text color
                ),

                onClick = {
                    navController.navigate(Screen.VideoList.rout)
                }
            ) {
                Text(
                    text = "Access",
                    style = TextStyle(
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold
                    ),
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

//private fun setOnClick(context: Context) {
//    Intent(context, VideoListActivity::class.java).also {
//        context.startActivity(it)
//    }
//}