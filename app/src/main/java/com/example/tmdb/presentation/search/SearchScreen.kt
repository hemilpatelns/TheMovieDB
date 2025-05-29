package com.example.tmdb.presentation.search

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.tmdb.domain.util.FunctionUtil
import com.example.tmdb.presentation.components.CommonVideoCard
import com.example.tmdb.presentation.components.SearchBar
import kotlinx.coroutines.delay

@Composable
fun SearchScreen(navController: NavHostController) {
    val focusRequester = remember { FocusRequester() }
    val keyboardController = LocalSoftwareKeyboardController.current
    val isEdgeToEdge = FunctionUtil.isEdgeToEdgeEnabled(LocalView.current)
    val viewmodel = hiltViewModel<SearchViewModel>()
    val results = viewmodel.searchResults
    val searchQuery by viewmodel.searchQuery.collectAsState()
    val listState = rememberLazyGridState()
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        delay(100)
        focusRequester.requestFocus()
        keyboardController?.show()
    }
    LaunchedEffect(viewmodel.errorMessage) {
        viewmodel.errorMessage?.let {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
            viewmodel.errorMessage = null // reset after showing
        }
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = if (isEdgeToEdge) 60.dp else 0.dp)
    ) {
        SearchBar(
            query = searchQuery,
            onQueryChange = { viewmodel.onQueryChanged(it) },
            focusRequester = focusRequester
        )
        LazyVerticalGrid(
            state = listState,
            columns = GridCells.Fixed(2),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp),
            horizontalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            items(results.size){ index ->
                val item = results[index]
                CommonVideoCard(
                    video = item,
                    navController = navController,
                    200.dp,
                    250.dp
                )
                if (index == results.size - 1 && !viewmodel.isLoading) {
                    viewmodel.loadSearchResults()
                }
            }
            if (viewmodel.isLoading) {
                items(2){
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }
            }
        }
    }
}