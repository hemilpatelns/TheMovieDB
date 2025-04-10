package com.example.tmdb.presentation.search

import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tmdb.domain.model.SearchData
import com.example.tmdb.domain.repository.SearchResultsRepository
import com.example.tmdb.domain.util.Resource
import com.example.tmdb.presentation.list.SearchListState
import com.example.tmdb.presentation.list.VideoListUiEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val repository: SearchResultsRepository
): ViewModel() {
//    private var _searchListState = MutableStateFlow(SearchListState())
//    val searchListState = _searchListState.asStateFlow()
//    private val _query = MutableStateFlow("")
//    val query: StateFlow<String> = _query
//
//    init {
//
//    }
//
//    fun getSearchResults(query: String) {
//        val page = searchListState.value.searchListPage
//        val searchList = searchListState.value.searchList
//
//        viewModelScope.launch(Dispatchers.IO) {
//            _searchListState.update {
//                it.copy(isLoading = true)
//            }
//            repository.getSearchResults(
//                query,
//                page
//            ).collectLatest { result ->
//                when(result){
//                    is Resource.Error -> {
//                        _searchListState.update {
//                            it.copy(isLoading = false)
//                        }
//                    }
//                    is Resource.Success -> {
//                        result.data?.let { list ->
//                            _searchListState.update {
//                                it.copy(
//                                    searchList = searchList + list,
//                                    searchListPage = page + 1,
//                                    isLoading = false
//                                )
//                            }
//                        }
//                    }
//                    is Resource.Loading -> {
//                        _searchListState.update {
//                            it.copy(isLoading = true)
//                        }
//                    }
//                }
//            }
//        }
//    }

    private val _searchQuery = MutableStateFlow("")
    val searchQuery = _searchQuery.asStateFlow()
    var currentPage by mutableStateOf(1)
    var totalPages by mutableStateOf(1)
    var isLoading by mutableStateOf(false)
    var errorMessage by mutableStateOf<String?>(null)

    var searchResults = mutableStateListOf<SearchData>()

    init {
        observeDebouncedQuery()
    }

    fun onQueryChanged(query: String) {
        _searchQuery.value = query
    }

    @OptIn(FlowPreview::class)
    private fun observeDebouncedQuery() {
        viewModelScope.launch {
            _searchQuery
                .debounce(1000L)
                .distinctUntilChanged()
                .collectLatest { query ->
                    if (query.isBlank()) {
                        // Clear everything on empty input
                        currentPage = 1
                        totalPages = 1
                        searchResults.clear()
                        return@collectLatest
                    }

                    // New search
                    currentPage = 1
                    totalPages = 1
                    searchResults.clear()
                    loadSearchResults(query)
                }
        }
    }

    fun loadSearchResults(query: String = _searchQuery.value) {
        if (isLoading || currentPage > totalPages) return

        viewModelScope.launch {
            repository.getSearchResults(query, currentPage)
                .collect { resource ->
                    when (resource) {
                        is Resource.Loading -> {
                            isLoading = resource.isLoading
                        }
                        is Resource.Success -> {
                            resource.data?.let { response ->
                                totalPages = response.totalPages
                                currentPage++
                                response.results?.let { searchResults.addAll(it) }
                            }
                        }
                        is Resource.Error -> {
                            errorMessage = resource.message
                        }
                    }
                }
        }
    }
}