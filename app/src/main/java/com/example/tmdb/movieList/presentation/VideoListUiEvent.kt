package com.example.tmdb.movieList.presentation

sealed interface VideoListUiEvent {
    data class Paginate(val category: String): VideoListUiEvent
    data object Navigate: VideoListUiEvent
}