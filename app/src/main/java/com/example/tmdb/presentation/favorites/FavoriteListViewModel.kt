package com.example.tmdb.presentation.favorites

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tmdb.domain.repository.MovieListRepository
import com.example.tmdb.domain.repository.SeriesRepository
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
class FavoriteListViewModel @Inject constructor(
    private val movieListRepository: MovieListRepository,
    private val seriesListRepository: SeriesRepository
) : ViewModel() {

    private var _favoriteListState = MutableStateFlow(FavoriteListState())
    val favoriteListState = _favoriteListState.asStateFlow()

//    init {
//        getFavoriteMovies()
//        getFavoriteSeries()
//    }

    fun getFavoriteMovies() {
        viewModelScope.launch(Dispatchers.IO) {
            _favoriteListState.update {
                it.copy(isLoading = true)
            }
            movieListRepository.getFavoriteMovies()
                .collectLatest { result ->
                    when(result) {
                        is Resource.Error -> {
                            _favoriteListState.update {
                                it.copy(isLoading = false)
                            }
                        }
                        is Resource.Success -> {
                            result.data?.let { list ->
                                _favoriteListState.update {
                                    it.copy(
                                        isLoading = false,
                                        favoriteMovieList = list.map { movie ->
                                            movie.copy(
                                                isFavorite = true
                                            )
                                        }
                                    )
                                }
                            }
                        }
                        is Resource.Loading -> {
                            _favoriteListState.update {
                                it.copy(isLoading = true)
                            }
                        }
                    }
                }
        }
    }

    fun getFavoriteSeries() {
        viewModelScope.launch(Dispatchers.IO) {
            _favoriteListState.update {
                it.copy(isLoading = true)
            }
            seriesListRepository.getFavoriteSeries()
                .collectLatest { result ->
                    when(result) {
                        is Resource.Error -> {
                            _favoriteListState.update {
                                it.copy(isLoading = false)
                            }
                        }
                        is Resource.Success -> {
                            result.data?.let { list ->
                                _favoriteListState.update {
                                    it.copy(
                                        isLoading = false,
                                        favoriteSeriesList = list.map { series ->
                                            series.copy(
                                                isFavorite = true
                                            )
                                        }
                                    )
                                }
                            }
                        }
                        is Resource.Loading -> {
                            _favoriteListState.update {
                                it.copy(isLoading = true)
                            }
                        }
                    }
                }
        }
    }

    fun toggleFavorite(videoId: Int, type: String) {
        viewModelScope.launch {
            val currentState = _favoriteListState.value
            var newFavorite: Boolean? = null

            when (type) {
                "movie" -> {
                    val updatedMovies = currentState.favoriteMovieList.map { movie ->
                        if (movie.id == videoId) {
                            newFavorite = !movie.isFavorite
                            movie.copy(isFavorite = newFavorite!!)
                        } else movie
                    }

                    if (newFavorite == true) movieListRepository.addFavorite(videoId)
                    else if (newFavorite == false) movieListRepository.removeFavorite(videoId)

                    _favoriteListState.update {
                        it.copy(favoriteMovieList = updatedMovies)
                    }
                }

                "series" -> {
                    val updatedSeries = currentState.favoriteSeriesList.map { series ->
                        if (series.id == videoId) {
                            newFavorite = !series.isFavorite
                            series.copy(isFavorite = newFavorite!!)
                        } else series
                    }

                    if (newFavorite == true) seriesListRepository.addFavorite(videoId)
                    else if (newFavorite == false) seriesListRepository.removeFavorite(videoId)

                    _favoriteListState.update {
                        it.copy(favoriteSeriesList = updatedSeries)
                    }
                }
            }
        }
    }
}