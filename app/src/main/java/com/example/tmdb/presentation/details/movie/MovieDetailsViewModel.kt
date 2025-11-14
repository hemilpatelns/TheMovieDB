package com.example.tmdb.presentation.details.movie

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tmdb.domain.repository.MovieListRepository
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
class MovieDetailsViewModel @Inject constructor(
    private val movieListRepository: MovieListRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val movieId = savedStateHandle.get<Int>("movieId")

    private var _movieDetailsState = MutableStateFlow(MovieDetailsState())
    val movieDetailsState = _movieDetailsState.asStateFlow()

    init {
//        getMovie(movieId ?: -1)
        getMovieFromApi(movieId ?: -1)
        checkIfFavorite(movieId ?: -1)
    }

    private fun getMovie(id: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            _movieDetailsState.update {
                it.copy(isLoading = true)
            }

            movieListRepository.getMovie(id).collectLatest { result ->
                when (result) {
                    is Resource.Error -> {
                        _movieDetailsState.update {
                            it.copy(isLoading = false)
                        }
                    }

                    is Resource.Loading -> {
                        _movieDetailsState.update {
                            it.copy(isLoading = result.isLoading)
                        }
                    }

                    is Resource.Success -> {
                        result.data?.let { movie ->
                            _movieDetailsState.update {
                                it.copy(movie = movie)
                            }
                        }
                    }
                }
            }
        }
    }

    fun getMovieFromApi(id: Int){
        viewModelScope.launch(Dispatchers.IO) {
            _movieDetailsState.update {
                it.copy(isLoading = true)
            }

            movieListRepository.getMovieFromApi(id).collectLatest { result ->
                when (result) {
                    is Resource.Error -> {
                        _movieDetailsState.update {
                            it.copy(isLoading = false)
                        }
                    }

                    is Resource.Loading -> {
                        _movieDetailsState.update {
                            it.copy(isLoading = result.isLoading)
                        }
                    }

                    is Resource.Success -> {
                        result.data?.let { movie ->
                            _movieDetailsState.update {
                                it.copy(movie = movie)
                            }
                        }
                    }
                }
            }
        }
    }

    fun toggleFavorite(movieId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            val newFavoriteState = !_movieDetailsState.value.isFavorite
            if (newFavoriteState) {
                movieListRepository.addFavorite(movieId)
            } else {
                movieListRepository.removeFavorite(movieId)
            }
            // Update the favorite state in the MovieDetailsState
            _movieDetailsState.update { it.copy(isFavorite = newFavoriteState) }
        }
    }

    private fun checkIfFavorite(movieId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            movieListRepository.isFavorite(movieId).collect { isFav ->
                _movieDetailsState.update { it.copy(isFavorite = isFav) }
            }
        }
    }
}