package com.example.tmdb.presentation.list

sealed interface VideoListUiEvent {
    data class Paginate(val category: String): VideoListUiEvent
    data object Navigate: VideoListUiEvent
}