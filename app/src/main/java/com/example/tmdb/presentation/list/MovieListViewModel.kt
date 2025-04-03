package com.example.tmdb.presentation.list

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
//        getPopularMovieList(false)
//        getUpcomingMovieList(false)
        getMovieList(MovieCategory.NOW_PLAYING)
        getMovieList(MovieCategory.POPULAR)
        getMovieList(MovieCategory.TOP_RATED)
        getMovieList(MovieCategory.UPCOMING)
    }

    fun onEvent(event: VideoListUiEvent) {
        when (event) {
            VideoListUiEvent.Navigate -> {

            }

            is VideoListUiEvent.Paginate -> {
                when (event.category) {
                    MovieCategory.NOW_PLAYING,
                    MovieCategory.POPULAR,
                    MovieCategory.TOP_RATED,
                    MovieCategory.UPCOMING -> getMovieList(event.category)
                }
            }
        }
    }
//    private fun getPopularMovieList(forceFetchFromRemote: Boolean) {
//        viewModelScope.launch(Dispatchers.IO) {
//            _movieListState.update {
//                it.copy(isLoading = true)
//            }
//            movieListRepository.getMovieList(
////                forceFetchFromRemote,
//                Category.POPULAR,
//                movieListState.value.popularMovieListPage
//            ).collectLatest { result ->
//                when(result){
//                    is Resource.Error -> {
//                        _movieListState.update {
//                            it.copy(isLoading = false)
//                        }
//                    }
//                    is Resource.Success -> {
//                        result.data?.let {popularList ->
//                            _movieListState.update {
//                                it.copy(
//                                    popularMovieList = movieListState.value.popularMovieList + popularList,
//                                    popularMovieListPage = movieListState.value.popularMovieListPage + 1
//                                )
//                            }
//                        }
//                    }
//                    is Resource.Loading -> {
//                        _movieListState.update {
//                            it.copy(isLoading = result.isLoading)
//                        }
//                    }
//                }
//            }
//        }
//    }

//    private fun getUpcomingMovieList(forceFetchFromRemote: Boolean) {
//        viewModelScope.launch(Dispatchers.IO) {
//            _movieListState.update {
//                it.copy(isLoading = true)
//            }
//            movieListRepository.getMovieList(
////                forceFetchFromRemote,
//                Category.UPCOMING,
//                movieListState.value.upcomingMovieListPage
//            ).collectLatest { result ->
//                when(result){
//                    is Resource.Error -> {
//                        _movieListState.update {
//                            it.copy(isLoading = false)
//                        }
//                    }
//                    is Resource.Success -> {
//                        result.data?.let {upcomingList ->
//                            _movieListState.update {
//                                it.copy(
//                                    upcomingMovieList = movieListState.value.upcomingMovieList + upcomingList,
//                                    upcomingMovieListPage = movieListState.value.upcomingMovieListPage + 1
//                                )
//                            }
//                        }
//                    }
//                    is Resource.Loading -> {
//                        _movieListState.update {
//                            it.copy(isLoading = result.isLoading)
//                        }
//                    }
//                }
//            }
//        }
//    }

    private fun getMovieList(category: String) {
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

        viewModelScope.launch(Dispatchers.IO) {
            _movieListState.update {
                it.copy(isLoading = true)
            }
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
        }
    }

    fun getFavoriteMovies() {
        viewModelScope.launch(Dispatchers.IO) {
            _movieListState.update {
                it.copy(isLoading = true)
            }
            movieListRepository.getFavoriteMovies()
                .collectLatest { result ->
                    when(result) {
                        is Resource.Error -> {
                            _movieListState.update {
                                it.copy(isLoading = false)
                            }
                        }
                        is Resource.Success -> {
                            result.data?.let { list ->
                                _movieListState.update {
                                    it.copy(favoriteMovieList = list)
                                }
                            }
                        }
                        is Resource.Loading -> {
                            _movieListState.update {
                                it.copy(isLoading = false)
                            }
                        }
                    }
                }
        }
    }
}