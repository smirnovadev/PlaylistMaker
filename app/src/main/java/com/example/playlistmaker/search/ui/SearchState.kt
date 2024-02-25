package com.example.playlistmaker.search.ui

import com.example.playlistmaker.search.domain.model.Track

sealed class SearchState {
    object Error: SearchState()
    object Loading: SearchState()
    data class Content(val tracks: List<Track>): SearchState()
    data class History(val tracks: List<Track>): SearchState()

}