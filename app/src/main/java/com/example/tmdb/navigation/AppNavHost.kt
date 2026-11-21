package com.example.tmdb.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.example.tmdb.domain.util.Screen
import com.example.tmdb.presentation.details.movie.VideoDetails
import com.example.tmdb.presentation.details.series.SeriesDetails
import com.example.tmdb.presentation.home.HomeScreen
import com.example.tmdb.presentation.list.BottomNavigation
import com.example.tmdb.presentation.search.SearchScreen

@Composable
fun AppNavHost(
    navController: NavHostController = rememberNavController()
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Home
    ) {
        composable<Screen.Home> {
            HomeScreen(
                onAccessClick = {
                    navController.navigate(Screen.BottomNav)
                }
            )
        }

        composable<Screen.BottomNav> {
            BottomNavigation(
                onNavigateToMovieDetails = { movieId ->
                    navController.navigate(Screen.Details(movieId))
                },
                onNavigateToSeriesDetails = { seriesId ->
                    navController.navigate(Screen.SeriesDetails(seriesId))
                },
                onNavigateToSearch = {
                    navController.navigate(Screen.Search)
                }
            )
        }

        composable<Screen.Details> { backStackEntry ->
            val route: Screen.Details = backStackEntry.toRoute()
            VideoDetails(
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }

        composable<Screen.SeriesDetails> { backStackEntry ->
            val route: Screen.SeriesDetails = backStackEntry.toRoute()
            SeriesDetails(
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }

        composable<Screen.Search> {
            SearchScreen(
                onNavigateToMovieDetails = { movieId ->
                    navController.navigate(Screen.Details(movieId))
                },
                onNavigateToSeriesDetails = { seriesId ->
                    navController.navigate(Screen.SeriesDetails(seriesId))
                }
            )
        }
    }
}
