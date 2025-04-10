package com.example.tmdb.domain.repository

import com.example.tmdb.domain.model.SearchData
import com.example.tmdb.domain.model.SearchResponse
import com.example.tmdb.domain.util.Resource
import kotlinx.coroutines.flow.Flow

interface SearchResultsRepository {

    suspend fun getSearchResults(
        query: String,
        page: Int
    ): Flow<Resource<SearchResponse>>
}