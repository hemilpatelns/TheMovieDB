package com.example.tmdb.presentation.list

import android.R.attr.category
import android.R.attr.type
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tmdb.domain.model.Movie
import com.example.tmdb.domain.repository.MovieListRepository
import com.example.tmdb.domain.util.MovieCategory
import com.example.tmdb.domain.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MovieListViewModel @Inject constructor(
    private val movieListRepository: MovieListRepository
) : ViewModel() {

    private var _movieListState = MutableStateFlow(MovieListState())
    val movieListState = _movieListState.asStateFlow()

    init {
        getMovieList(MovieCategory.NOW_PLAYING)
        getMovieList(MovieCategory.POPULAR)
        getMovieList(MovieCategory.TOP_RATED)
        getMovieList(MovieCategory.UPCOMING)
    }

    fun paginateList(category: String) {
        when (category) {
            MovieCategory.NOW_PLAYING,
            MovieCategory.POPULAR,
            MovieCategory.TOP_RATED,
            MovieCategory.UPCOMING -> getMovieList(category)
        }
    }

    fun getMovieList(category: String) {
        val moviePage: Int
        val movieList: List<Movie>

        when (category) {
            MovieCategory.NOW_PLAYING ->{
                moviePage = movieListState.value.nowPlayingMovieListPage
                movieList = movieListState.value.nowPlayingMovieList
            }
            MovieCategory.POPULAR -> {
                moviePage = movieListState.value.popularMovieListPage
                movieList = movieListState.value.popularMovieList
            }
            MovieCategory.TOP_RATED ->{
                moviePage = movieListState.value.topRatedMovieListPage
                movieList = movieListState.value.topRatedMovieList
            }
            MovieCategory.UPCOMING -> {
                moviePage = movieListState.value.upcomingMovieListPage
                movieList = movieListState.value.upcomingMovieList
            }

            else -> {
                throw IllegalArgumentException("Unknown category: $category")
            }
        }

        viewModelScope.launch {
//            _movieListState.update {
//                it.copy(isLoading = true)
//            }
            movieListRepository.getMovieList(
                category,
                moviePage
            ).collectLatest { result ->
                when (result) {
                    is Resource.Error -> {
                        _movieListState.update {
                            it.copy(isLoading = false)
                        }
                    }

                    is Resource.Success -> {
                        result.data?.let { list ->
                            _movieListState.update {
                                when (category) {
                                    MovieCategory.NOW_PLAYING -> it.copy(
                                        nowPlayingMovieList = movieList + list,
                                        nowPlayingMovieListPage = moviePage + 1,
                                        isLoading = false
                                    )
                                    MovieCategory.POPULAR -> it.copy(
                                        popularMovieList = movieList + list,
                                        popularMovieListPage = moviePage + 1,
                                        isLoading = false
                                    )
                                    MovieCategory.TOP_RATED -> it.copy(
                                        topRatedMovieList = movieList + list,
                                        topRatedMovieListPage = moviePage + 1,
                                        isLoading = false
                                    )
                                    MovieCategory.UPCOMING -> it.copy(
                                        upcomingMovieList = movieList + list,
                                        upcomingMovieListPage = moviePage + 1,
                                        isLoading = false
                                    )

                                    else -> it
                                }
                            }
                        }
                    }

                    is Resource.Loading -> {
                        _movieListState.update {
                            it.copy(isLoading = result.isLoading)
                        }
                    }
                }
            }
            movieListRepository.getFavoriteMovieIds().collectLatest { favoriteIds ->
                _movieListState.update { state ->
                    state.copy(
                        nowPlayingMovieList = state.nowPlayingMovieList.map { it.copy(isFavorite = it.id in favoriteIds) },
                        popularMovieList = state.popularMovieList.map { it.copy(isFavorite = it.id in favoriteIds) },
                        topRatedMovieList = state.topRatedMovieList.map { it.copy(isFavorite = it.id in favoriteIds) },
                        upcomingMovieList = state.upcomingMovieList.map { it.copy(isFavorite = it.id in favoriteIds) },
                        favoriteMovieList = state.favoriteMovieList.map { it.copy(isFavorite = it.id in favoriteIds) }
                    )
                }
            }
        }
    }

    fun toggleFavorite(movieId: Int) {
        viewModelScope.launch {
            val movie = _movieListState.value.nowPlayingMovieList
                .plus(_movieListState.value.popularMovieList)
                .plus(_movieListState.value.topRatedMovieList)
                .plus(_movieListState.value.upcomingMovieList)
                .firstOrNull { it.id == movieId }

            movie?.let {
                if (it.isFavorite) {
                    movieListRepository.removeFavorite(it.id)
                } else {
                    movieListRepository.addFavorite(it.id)
                }
            }
        }
    }

    fun observeFavoriteIds() {
        viewModelScope.launch {
            movieListRepository.getFavoriteMovieIds().collectLatest { favoriteIds ->
                _movieListState.update { state ->
                    state.copy(
                        nowPlayingMovieList = state.nowPlayingMovieList.map { it.copy(isFavorite = it.id in favoriteIds) },
                        popularMovieList = state.popularMovieList.map { it.copy(isFavorite = it.id in favoriteIds) },
                        topRatedMovieList = state.topRatedMovieList.map { it.copy(isFavorite = it.id in favoriteIds) },
                        upcomingMovieList = state.upcomingMovieList.map { it.copy(isFavorite = it.id in favoriteIds) },
                        favoriteMovieList = state.favoriteMovieList.map { it.copy(isFavorite = it.id in favoriteIds) }
                    )
                }
            }
        }
    }

    fun onMovieLongClick(category: String, movieId: Int) {
        _movieListState.update { currentState ->
            when(category) {
                MovieCategory.NOW_PLAYING -> {
                    currentState.copy(
                        nowPlayingMovieList = currentState.nowPlayingMovieList.map {
                            if(it.id == movieId){
                                it.copy(
                                    showLongClickUi = !it.showLongClickUi
                                )
                            } else it
                        }
                    )
                }

                MovieCategory.POPULAR -> {
                    currentState.copy(
                        popularMovieList = currentState.popularMovieList.map {
                            if(it.id == movieId){
                                it.copy(
                                    showLongClickUi = !it.showLongClickUi
                                )
                            } else it
                        }
                    )
                }

                MovieCategory.UPCOMING -> {
                    currentState.copy(
                        upcomingMovieList = currentState.upcomingMovieList.map {
                            if(it.id == movieId){
                                it.copy(
                                    showLongClickUi = !it.showLongClickUi
                                )
                            } else it
                        }
                    )
                }

                MovieCategory.TOP_RATED -> {
                    currentState.copy(
                        topRatedMovieList = currentState.topRatedMovieList.map {
                            if(it.id == movieId){
                                it.copy(
                                    showLongClickUi = !it.showLongClickUi
                                )
                            } else it
                        }
                    )
                }

                else -> currentState
            }
        }
    }
}