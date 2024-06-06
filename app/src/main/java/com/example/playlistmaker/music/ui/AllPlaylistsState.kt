package com.example.playlistmaker.music.ui

import com.example.playlistmaker.createPlaylist.domain.model.Playlist


sealed class AllPlaylistsState {
    object Empty: AllPlaylistsState()
    data class Content(val playlist: List<Playlist>) : AllPlaylistsState()
}