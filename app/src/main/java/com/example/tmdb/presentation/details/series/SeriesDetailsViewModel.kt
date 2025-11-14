package com.example.tmdb.presentation.details.series

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
class SeriesDetailsViewModel @Inject constructor(
    private val seriesRepository: SeriesRepository,
    savedStateHandle: SavedStateHandle
): ViewModel() {
    private val seriesId = savedStateHandle.get<Int>("seriesId")

    private var _seriesDetailsState = MutableStateFlow(SeriesDetailsState())
    val seriesDetailsState = _seriesDetailsState.asStateFlow()

    init {
        getSeriesFromApi(seriesId ?: -1)
        checkIfFavorite(seriesId ?: -1)
    }

    private fun getSeriesFromApi(id: Int){
        viewModelScope.launch(Dispatchers.IO) {
            _seriesDetailsState.update {
                it.copy(isLoading = true)
            }

            seriesRepository.getSeries(id).collectLatest { result ->
                when (result) {
                    is Resource.Error -> {
                        _seriesDetailsState.update {
                            it.copy(isLoading = false)
                        }
                    }

                    is Resource.Loading -> {
                        _seriesDetailsState.update {
                            it.copy(isLoading = result.isLoading)
                        }
                    }

                    is Resource.Success -> {
                        result.data?.let { series ->
                            _seriesDetailsState.update {
                                it.copy(series = series)
                            }
                        }
                    }
                }
            }
        }
    }

    fun toggleFavorite(seriesId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            val newFavoriteState = !_seriesDetailsState.value.isFavorite
            if (newFavoriteState) {
                seriesRepository.addFavorite(seriesId)
            } else {
                seriesRepository.removeFavorite(seriesId)
            }
            // Update the favorite state in the MovieDetailsState
            _seriesDetailsState.update { it.copy(isFavorite = newFavoriteState) }
        }
    }

    private fun checkIfFavorite(seriesId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            seriesRepository.isFavorite(seriesId).collect { isFav ->
                _seriesDetailsState.update { it.copy(isFavorite = isFav) }
            }
        }
    }
}