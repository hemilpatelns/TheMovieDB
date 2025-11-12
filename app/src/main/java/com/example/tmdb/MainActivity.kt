package com.example.tmdb

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.tmdb.presentation.home.HomeScreen
import com.example.tmdb.presentation.list.BottomNavigation
import com.example.tmdb.domain.util.Screen
import com.example.tmdb.presentation.details.SeriesDetails
import com.example.tmdb.presentation.details.VideoDetails
import com.example.tmdb.presentation.search.SearchScreen
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val navController = rememberNavController()

            NavHost(
                navController = navController,
                startDestination = Screen.Home.rout
            ) {
                composable(Screen.Home.rout) {
                    HomeScreen(navController)
                }
                composable(Screen.BottomNav.rout) {
                    BottomNavigation(navController)
                }
                composable(
                    Screen.Details.rout + "/{movieId}",
                    arguments = listOf(
                        navArgument("movieId") {
                            type = NavType.IntType
                        }
                    )
                ) {
                    VideoDetails(navController)
                }
                composable(
                    Screen.SeriesDetails.rout + "/{seriesId}",
                    arguments = listOf(
                        navArgument("seriesId") {
                            type = NavType.IntType
                        }
                    )
                ) {
                    SeriesDetails(navController)
                }
                composable(Screen.Search.rout) {
                    SearchScreen(navController)
                }
            }
        }
        WindowCompat.setDecorFitsSystemWindows(window, false)

        WindowInsetsControllerCompat(window, window.decorView).let { controller ->
            controller.hide(WindowInsetsCompat.Type.systemBars())
            controller.systemBarsBehavior =
                WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        }
    }
}

