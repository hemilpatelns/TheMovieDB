package com.example.tmdb.graph

import androidx.compose.runtime.Composable
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.NavHost
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navigation
import com.example.tmdb.presentation.list.FavoriteScreen
import com.example.tmdb.presentation.details.SeriesDetails
import com.example.tmdb.domain.util.Screen
import com.example.tmdb.presentation.details.VideoDetails
import com.example.tmdb.presentation.list.VideoList

@Composable
fun ListNavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        route = Graph.LIST,
        startDestination = Screen.VideoList.rout
    ) {
        composable(route = Screen.VideoList.rout) {
            VideoList(navController)
        }
        composable(route = Screen.Favorites.rout) {
            FavoriteScreen(navController)
        }
        detailsNavGraph(navController)
    }
}

fun NavGraphBuilder.detailsNavGraph(navController: NavHostController) {
    navigation(
        route = Graph.DETAILS,
        startDestination = Screen.Details.rout
    ) {
        composable(
            route = Screen.Details.rout + "/movieId",
            arguments = listOf(
                navArgument("movieId") {
                    type = NavType.IntType
                }
            )
        ) {
            VideoDetails(navController)
        }
        composable(
            route = Screen.SeriesDetails.rout + "/seriesId",
            arguments = listOf(
                navArgument("seriesId") {
                    type = NavType.IntType
                }
            )
        ) {
            SeriesDetails(navController)
        }
    }
}