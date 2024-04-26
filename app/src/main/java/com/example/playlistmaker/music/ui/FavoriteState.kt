package com.example.playlistmaker.music.ui

import com.example.playlistmaker.search.domain.model.Track

sealed class FavoriteState {
    object Empty: FavoriteState()
    data class Content(val favoriteTrack: List<Track>) : FavoriteState()
}