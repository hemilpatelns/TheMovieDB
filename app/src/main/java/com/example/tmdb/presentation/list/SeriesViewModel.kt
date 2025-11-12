package com.example.tmdb.presentation.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tmdb.domain.model.Series
import com.example.tmdb.domain.repository.SeriesRepository
import com.example.tmdb.domain.util.Resource
import com.example.tmdb.domain.util.SeriesCategory
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SeriesViewModel @Inject constructor(
    private val seriesRepository: SeriesRepository
): ViewModel() {

    private var _seriesListState = MutableStateFlow(SeriesListState())
    val seriesListState = _seriesListState.asStateFlow()

    init {
        getSeriesList(SeriesCategory.AIRING_TODAY)
        getSeriesList(SeriesCategory.ON_THE_AIR)
        getSeriesList(SeriesCategory.POPULAR)
        getSeriesList(SeriesCategory.TOP_RATED)
    }

    fun onEvent(event: VideoListUiEvent){
        when(event){
            VideoListUiEvent.Navigate -> {

            }
            is VideoListUiEvent.Paginate -> {
                when (event.category) {
                    SeriesCategory.AIRING_TODAY,
                    SeriesCategory.ON_THE_AIR,
                    SeriesCategory.POPULAR,
                    SeriesCategory.TOP_RATED -> getSeriesList(event.category)
                }
            }
        }
    }

    private fun getSeriesList(category: String) {
        val seriesPage: Int
        val seriesList: List<Series>

        when (category) {
            SeriesCategory.AIRING_TODAY -> {
                seriesPage = seriesListState.value.airingTodaySeriesListPage
                seriesList = seriesListState.value.airingTodaySeriesList
            }
            SeriesCategory.ON_THE_AIR -> {
                seriesPage = seriesListState.value.onTheAirSeriesListPage
                seriesList = seriesListState.value.onTheAirSeriesList
            }
            SeriesCategory.POPULAR -> {
                seriesPage = seriesListState.value.popularSeriesListPage
                seriesList = seriesListState.value.popularSeriesList
            }
            SeriesCategory.TOP_RATED -> {
                seriesPage = seriesListState.value.topRatedSeriesListPage
                seriesList = seriesListState.value.topRatedSeriesList
            }
            else -> {
                throw IllegalArgumentException("Unknown category: $category")
            }
        }

        viewModelScope.launch {
//            _seriesListState.update {
//                it.copy(isLoading = true)
//            }
            seriesRepository.getSeriesList(
                category,
                seriesPage
            ).collectLatest { result ->
                when (result) {
                    is Resource.Error -> {
                        _seriesListState.update {
                            it.copy(isLoading = false)
                        }
                    }
                    is Resource.Success -> {
                        result.data?.let { list ->
                            _seriesListState.update {
                                when (category) {
                                    SeriesCategory.AIRING_TODAY -> it.copy(
                                        airingTodaySeriesList = seriesList + list,
                                        airingTodaySeriesListPage = seriesPage + 1,
                                        isLoading = false
                                    )
                                    SeriesCategory.ON_THE_AIR -> it.copy(
                                        onTheAirSeriesList = seriesList + list,
                                        onTheAirSeriesListPage = seriesPage + 1,
                                        isLoading = false
                                    )
                                    SeriesCategory.POPULAR -> it.copy(
                                        popularSeriesList = seriesList + list,
                                        popularSeriesListPage = seriesPage + 1,
                                        isLoading = false
                                    )
                                    SeriesCategory.TOP_RATED -> it.copy(
                                        topRatedSeriesList = seriesList + list,
                                        topRatedSeriesListPage = seriesPage + 1,
                                        isLoading = false
                                    )
                                    else -> it
                                }
                            }
                        }
                    }
                    is Resource.Loading -> {
                        _seriesListState.update {
                            it.copy(isLoading = result.isLoading)
                        }
                    }
                }
            }
            seriesRepository.getFavoriteSeriesIds().collectLatest { favoriteIds ->
                _seriesListState.update { state ->
                    state.copy(
                        airingTodaySeriesList = state.airingTodaySeriesList.map { it.copy(isFavorite = it.id in favoriteIds) },
                        onTheAirSeriesList = state.onTheAirSeriesList.map { it.copy(isFavorite = it.id in favoriteIds) },
                        popularSeriesList = state.popularSeriesList.map { it.copy(isFavorite = it.id in favoriteIds) },
                        topRatedSeriesList = state.topRatedSeriesList.map { it.copy(isFavorite = it.id in favoriteIds) },
                        favoriteSeriesList = state.favoriteSeriesList.map { it.copy(isFavorite = it.id in favoriteIds) }
                    )
                }
            }
        }
    }

    fun toggleFavorite(seriesId: Int) {
        viewModelScope.launch {
            val movie = _seriesListState.value.airingTodaySeriesList
                .plus(_seriesListState.value.onTheAirSeriesList)
                .plus(_seriesListState.value.popularSeriesList)
                .plus(_seriesListState.value.topRatedSeriesList)
                .firstOrNull { it.id == seriesId }

            movie?.let {
                if (it.isFavorite) {
                    seriesRepository.removeFavorite(it.id)
                } else {
                    seriesRepository.addFavorite(it.id)
                }
            }
        }
    }

    fun getFavoriteSeries() {
        viewModelScope.launch(Dispatchers.IO) {
            _seriesListState.update {
                it.copy(isLoading = true)
            }
            seriesRepository.getFavoriteSeries()
                .collectLatest { result ->
                    when(result) {
                        is Resource.Error -> {
                            _seriesListState.update {
                                it.copy(isLoading = false)
                            }
                        }
                        is Resource.Success -> {
                            result.data?.let { list ->
                                _seriesListState.update {
                                    it.copy(
                                        isLoading = false,
                                        favoriteSeriesList = list
                                    )
                                }
                            }
                        }
                        is Resource.Loading -> {
                            _seriesListState.update {
                                it.copy(isLoading = true)
                            }
                        }
                    }
                }
        }
    }
}