package com.example.tmdb.presentation.list

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.List
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.tmdb.presentation.details.SeriesDetails
import com.example.tmdb.domain.util.Screen
import com.example.tmdb.domain.util.toTitleCase
import com.example.tmdb.presentation.details.VideoDetails
import com.example.tmdb.presentation.search.SearchScreen
import com.example.tmdb.ui.theme.gradientBrushOne


@Composable
fun BottomNavigation() {
    val nestedNavController = rememberNavController()
    val navBackStackEntry by nestedNavController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    // Routes where bottom bar should be visible
    val bottomBarRoutes = listOf(Screen.VideoList.rout, Screen.Favorites.rout)
    Scaffold(
        contentWindowInsets = WindowInsets(0.dp),
        bottomBar = {
            if (currentRoute in bottomBarRoutes) {
                BottomNavigationBar(nestedNavController)
            }
        }
    ) {
        NavHost(
            modifier = Modifier
                .padding(it)
                .background(gradientBrushOne),
            navController = nestedNavController,
            startDestination = Screen.VideoList.rout
        ) {
            composable(Screen.VideoList.rout) {
                VideoList(nestedNavController)
            }
            composable(Screen.Favorites.rout) {
                FavoriteScreen(nestedNavController)
            }
            composable(
                Screen.Details.rout + "/{movieId}",
                arguments = listOf(
                    navArgument("movieId") {
                        type = NavType.IntType
                    }
                )
            ) {
                VideoDetails(nestedNavController)
            }
            composable(
                Screen.SeriesDetails.rout + "/{seriesId}",
                arguments = listOf(
                    navArgument("seriesId") {
                        type = NavType.IntType
                    }
                )
            ) {
                SeriesDetails(nestedNavController)
            }
            composable(Screen.Search.rout) {
                SearchScreen(nestedNavController)
            }
        }
    }
}

@Composable
fun BottomNavigationBar(navController: NavHostController) {
    val items = listOf(
        Screen.VideoList,
        Screen.Favorites
    )
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    NavigationBar(
        containerColor = Color(0xFD303243),
        tonalElevation = 10.dp
    ) {
        items.forEach { screen ->
            NavigationBarItem(
                icon = {
                    Icon(
                        imageVector = if (screen == Screen.VideoList) Icons.Default.List else Icons.Default.Favorite,
                        contentDescription = null
                    )
                },
                label = {
                    Text(
                        text = screen.rout.toTitleCase()
                    )
                },
                selected = currentRoute == screen.rout,
                onClick = {
                    navController.navigate(screen.rout) {
                        popUpTo(navController.graph.startDestinationId) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = Color(0xFF8000FF),
                    selectedTextColor = Color(0xFF8000FF),
                    indicatorColor = Color(0xFD303243),
                    unselectedIconColor = Color.White,
                    unselectedTextColor = Color.White,
                )
            )
        }
    }


}