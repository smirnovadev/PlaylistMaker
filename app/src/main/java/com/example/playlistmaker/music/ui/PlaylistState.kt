package com.example.playlistmaker.music.ui

import com.example.playlistmaker.playlist.domain.model.Playlist


sealed class PlaylistState {
    object Empty: PlaylistState()
    data class Content(val playlist: List<Playlist>) : PlaylistState()
}