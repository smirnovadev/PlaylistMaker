package com.example.playlistmaker.player.ui

import com.example.playlistmaker.createPlaylist.domain.model.Playlist

sealed class TrackAddedState {
    data class Success(val playlist: Playlist): TrackAddedState()
    data class Fail(val playlist: Playlist): TrackAddedState()
}
