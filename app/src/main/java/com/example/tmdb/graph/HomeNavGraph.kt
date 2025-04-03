package com.example.tmdb.graph

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.example.tmdb.presentation.home.HomeScreen
import com.example.tmdb.domain.util.Screen

fun NavGraphBuilder.homeNavGraph(navController: NavHostController) {
    navigation(
        route = Graph.HOME,
        startDestination = Screen.Home.rout
    ) {
        composable(route = Screen.Home.rout) {
            HomeScreen(navController)
        }
    }
}