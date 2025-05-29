package com.example.tmdb.presentation.search

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tmdb.domain.model.SearchData
import com.example.tmdb.domain.repository.SearchResultsRepository
import com.example.tmdb.domain.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val repository: SearchResultsRepository
): ViewModel() {
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