package com.example.tmdb.graph

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.tmdb.presentation.list.BottomNavigation

@Composable
fun RootNavigationGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        route = Graph.ROOT,
        startDestination = Graph.HOME
    ) {
        homeNavGraph(navController = navController)
        composable(route = Graph.LIST) {
            BottomNavigation()
        }
    }
}

object Graph {
    const val ROOT = "root_graph"
    const val HOME = "home_graph"
    const val LIST = "list_graph"
    const val DETAILS = "details_graph"
}