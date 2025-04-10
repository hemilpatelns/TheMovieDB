package com.example.tmdb.data.repository

import com.example.tmdb.data.remote.CommonApi
import com.example.tmdb.domain.model.SearchData
import com.example.tmdb.domain.model.SearchResponse
import com.example.tmdb.domain.repository.SearchResultsRepository
import com.example.tmdb.domain.util.Resource
import kotlinx.coroutines.flow.*
import javax.inject.Inject

class SearchResultsRepositoryImpl @Inject constructor(
    private val api: CommonApi
): SearchResultsRepository {

    override suspend fun getSearchResults(
        query: String,
        page: Int
    ): Flow<Resource<SearchResponse>> {
        return flow{
            emit(Resource.Loading(true))

            val searchData = try {
                api.getSearchResults(query, page)
            } catch(e: Exception){
                emit(Resource.Error("Error loading videos"))
                return@flow
            }
            emit(Resource.Success(searchData))
            emit(Resource.Loading(false))
        }
    }
}