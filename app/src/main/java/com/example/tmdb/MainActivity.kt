package com.example.tmdb

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
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
import com.example.tmdb.presentation.list.FavoriteScreen
import com.example.tmdb.presentation.list.VideoList
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
                    BottomNavigation()
                }
//                composable(Screen.Details.rout + "/{movieId}",
//                    arguments = listOf(
//                        navArgument("movieId") {
//                            type = NavType.IntType
//                        }
//                    )
//                ) {
//                    VideoDetails(navController)
//                }
//                composable(Screen.SeriesDetails.rout + "/{seriesId}",
//                    arguments = listOf(
//                        navArgument("seriesId") {
//                            type = NavType.IntType
//                        }
//                    )
//                ) {
//                    SeriesDetails(navController)
//                }
//                composable(Screen.Favorites.rout){
//                    FavoriteScreen(navController)
//                }
            }
        }
    }
}

