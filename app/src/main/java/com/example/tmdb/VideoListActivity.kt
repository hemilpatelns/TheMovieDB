package com.example.tmdb

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Warning
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import coil.size.Size
import com.example.tmdb.movieList.data.remote.MovieApi
import com.example.tmdb.movieList.domain.model.Movie
import com.example.tmdb.movieList.presentation.MovieListState
import com.example.tmdb.movieList.presentation.MovieListUiEvent
import com.example.tmdb.movieList.presentation.MovieListViewModel
import com.example.tmdb.movieList.util.Category
import com.example.tmdb.movieList.util.Screen
import com.example.tmdb.ui.theme.gradientBrushOne

@Composable
fun VideoList(
    navController: NavHostController,
) {
    val movieListViewModel = hiltViewModel<MovieListViewModel>()
    val movieListState = movieListViewModel.movieListState.collectAsState().value
    var selectedCategory by remember {
        mutableStateOf("Movies")
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .background(gradientBrushOne)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 30.dp)
                .padding(top = 50.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "What do you want to watch today?",
                style = TextStyle(
                    fontSize = 30.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                ),
                modifier = Modifier.weight(.7f),
            )
            Spacer(modifier = Modifier.width(15.dp))
            Image(
                modifier = Modifier
                    .size(40.dp)
                    .border(2.dp, Color.White, RoundedCornerShape(50))
                    .padding(3.dp),
                imageVector = ImageVector.vectorResource(id = R.drawable.ic_person),
                contentDescription = ""
            )
        }
        SearchScreen()
        CategorySelector(
            categories = listOf("Movies", "Series", "Anime", "Novels", "Documentaries"),
            onCategorySelected = {
                selectedCategory = it
            }
        )
        PopularVideoList(
            "Most Popular",
            movieListState = movieListState,
            navController = navController,
            onEvent = movieListViewModel::onEvent
        )
        UpcomingVideoList(
            "Upcoming",
            movieListState = movieListState,
            navController = navController,
            onEvent = movieListViewModel::onEvent
        )
    }
}

@Composable
fun SearchBar(
    query: String,
    onQueryChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 20.dp, horizontal = 30.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // TextField for input
        TextField(
            value = query,
            onValueChange = { onQueryChange(it) },
            placeholder = { Text("Search", color = Color(0x8FFFFFFF)) },
            singleLine = true,
            modifier = Modifier
                .weight(1f),
            trailingIcon = {
                Icon(
                    imageVector = ImageVector.vectorResource(id = R.drawable.ic_search), // Replace with your search icon
                    contentDescription = "Search",
                    tint = Color(0x8FFFFFFF)
                )
            },
            shape = RoundedCornerShape(50.dp),
            colors = TextFieldDefaults.colors(
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White,
                focusedContainerColor = Color(0xFF36076B),
                unfocusedContainerColor = Color(0xFF36076B),
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            )
        )
    }
}

@Composable
fun SearchScreen() {
    var searchQuery by remember { mutableStateOf("") }

    Column {
        SearchBar(
            query = searchQuery,
            onQueryChange = { searchQuery = it }
        )
    }
}

@Composable
fun VideoCard(movie: Movie, navController: NavHostController) {
    val context = LocalContext.current
    val imageState = rememberAsyncImagePainter(
        model = ImageRequest.Builder(context)
            .data(MovieApi.IMAGE_BASE_URL + movie.backdrop_path)
            .size(Size.ORIGINAL)
            .build()
    ).state

    Box(
        contentAlignment = Alignment.BottomCenter,
        modifier = Modifier
            .background(color = Color.Transparent)
            .width(150.dp)
            .height(230.dp)
            .clip(RoundedCornerShape(10))
            .clickable {
                navController.navigate(Screen.Details.rout + "/${movie.id}")
            },
    ) {
        if (imageState is AsyncImagePainter.State.Error) {
            Box(
                modifier = Modifier
                    .width(150.dp)
                    .height(230.dp)
                    .aspectRatio(.65f)
                    .clip(RoundedCornerShape(10)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Rounded.Warning,
                    contentDescription = movie.title
                )
            }
        }

        if (imageState is AsyncImagePainter.State.Success) {
            Image(
                modifier = Modifier
                    .width(150.dp)
                    .height(230.dp)
                    .aspectRatio(.65f)
                    .clip(RoundedCornerShape(10)),
                painter = imageState.painter,
                contentDescription = movie.title,
                contentScale = ContentScale.Crop
            )
        }
        Text(
            text = movie.title,
            color = Color.White,
            style = TextStyle(fontWeight = FontWeight.SemiBold),
            overflow = TextOverflow.Ellipsis,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = 5.dp, vertical = 8.dp)
        )
    }
}


@Composable
fun PopularVideoList(
    videoType: String,
    movieListState: MovieListState,
    navController: NavHostController,
    onEvent: (MovieListUiEvent) -> Unit
) {
    if (movieListState.popularMovieList.isEmpty()) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    } else {
        Text(
            text = videoType,
            style = TextStyle(fontSize = 18.sp, fontWeight = FontWeight.Bold),
            modifier = Modifier
                .padding(horizontal = 30.dp)
                .padding(top = 30.dp, bottom = 20.dp),
            color = Color.White
        )
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy((-48).dp)
        ) {
            items(movieListState.popularMovieList.size) { index ->
                Spacer(modifier = Modifier.padding(start = 30.dp))
                VideoCard(
                    movie = movieListState.popularMovieList[index],
                    navController = navController
                )
                Spacer(modifier = Modifier.padding(end = 30.dp))

                if (index >= movieListState.popularMovieList.size - 1 && !movieListState.isLoading) {
                    onEvent(MovieListUiEvent.Paginate(Category.POPULAR))
                }
            }
        }
    }
}

@Composable
fun UpcomingVideoList(
    videoType: String,
    movieListState: MovieListState,
    navController: NavHostController,
    onEvent: (MovieListUiEvent) -> Unit
) {
    if (movieListState.upcomingMovieList.isEmpty()) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    } else {
        Text(
            text = videoType,
            style = TextStyle(fontSize = 18.sp, fontWeight = FontWeight.Bold),
            modifier = Modifier
                .padding(horizontal = 30.dp)
                .padding(top = 30.dp, bottom = 20.dp),
            color = Color.White
        )
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy((-48).dp)
        ) {
            items(movieListState.upcomingMovieList.size) { index ->
                Spacer(modifier = Modifier.padding(start = 30.dp))
                VideoCard(
                    movie = movieListState.upcomingMovieList[index],
                    navController = navController
                )
                Spacer(modifier = Modifier.padding(end = 30.dp))

                if (index >= movieListState.upcomingMovieList.size - 1 && !movieListState.isLoading) {
                    onEvent(MovieListUiEvent.Paginate(Category.UPCOMING))
                }
            }
        }
    }
}

@Composable
fun CategoryItem(category: String, isSelected: Boolean, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .padding(vertical = 10.dp)
            .clickable { onClick() }
            .background(
                if (isSelected) Color(0xFFFF1F8A)
                else Color.Transparent,
                shape = RoundedCornerShape(50.dp)
            ),
        contentAlignment = Alignment.Center
    ) {
        Text(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp),
            text = category,
            color = if (isSelected) Color.White else Color(0x8FFFFFFF),
            fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal,
            fontSize = 16.sp
        )
    }
}

@Composable
fun CategorySelector(categories: List<String>, onCategorySelected: (String) -> Unit) {
    var selectedCategory by remember { mutableStateOf(categories.first()) }

    LazyRow(
        horizontalArrangement = Arrangement.spacedBy((-45).dp),
        modifier = Modifier
            .fillMaxWidth()
    ) {
        items(categories) { category ->
            Spacer(modifier = Modifier.padding(start = 30.dp))
            CategoryItem(
                category = category,
                isSelected = category == selectedCategory,
                onClick = {
                    selectedCategory = category
                    onCategorySelected(category)
                }
            )
            Spacer(modifier = Modifier.padding(start = 30.dp))
        }
    }
}
