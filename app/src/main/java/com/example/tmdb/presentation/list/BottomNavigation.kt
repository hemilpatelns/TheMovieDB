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
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.tmdb.domain.util.Screen
import com.example.tmdb.presentation.favorites.FavoriteScreen
import com.example.tmdb.ui.theme.gradientBrushOne

@Composable
fun BottomNavigation(
    onNavigateToMovieDetails: (Int) -> Unit,
    onNavigateToSeriesDetails: (Int) -> Unit,
    onNavigateToSearch: () -> Unit
) {
    val nestedNavController = rememberNavController()
    val navBackStackEntry by nestedNavController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    val bottomBarScreens = listOf(Screen.VideoList, Screen.Favorites)
    val isBottomBarVisible = bottomBarScreens.any { screen ->
        currentDestination?.hierarchy?.any { it.hasRoute(screen::class) } == true
    }

    Scaffold(
        contentWindowInsets = WindowInsets(0.dp),
        bottomBar = {
            if (isBottomBarVisible) {
                BottomNavigationBar(nestedNavController)
            }
        }
    ) { paddingValues ->
        NavHost(
            modifier = Modifier
                .padding(paddingValues)
                .background(gradientBrushOne),
            navController = nestedNavController,
            startDestination = Screen.VideoList
        ) {
            composable<Screen.VideoList> {
                VideoListScreen(
                    onNavigateToMovieDetails = onNavigateToMovieDetails,
                    onNavigateToSeriesDetails = onNavigateToSeriesDetails,
                    onNavigateToSearch = onNavigateToSearch
                )
            }
            composable<Screen.Favorites> {
                FavoriteScreen(
                    onNavigateToMovieDetails = onNavigateToMovieDetails,
                    onNavigateToSeriesDetails = onNavigateToSeriesDetails
                )
            }
        }
    }
}

@Composable
fun BottomNavigationBar(navController: NavHostController) {
    val items = listOf(Screen.VideoList, Screen.Favorites)
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    NavigationBar(
        containerColor = Color(0xFD303243),
        tonalElevation = 10.dp
    ) {
        items.forEach { screen ->
            val isSelected = currentDestination?.hierarchy?.any { it.hasRoute(screen::class) } == true
            val title = when (screen) {
                Screen.VideoList -> "List"
                Screen.Favorites -> "Favorites"
                else -> ""
            }
            NavigationBarItem(
                icon = {
                    Icon(
                        imageVector = if (screen == Screen.VideoList) Icons.Default.List else Icons.Default.Favorite,
                        contentDescription = title
                    )
                },
                label = { Text(text = title) },
                selected = isSelected,
                onClick = {
                    navController.navigate(screen) {
                        popUpTo(navController.graph.findStartDestination().id) {
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
