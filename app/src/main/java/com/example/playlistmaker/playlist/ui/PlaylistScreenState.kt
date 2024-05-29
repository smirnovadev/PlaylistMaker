package com.example.playlistmaker.playlist.ui

import com.example.playlistmaker.createPlaylist.domain.model.Playlist
import com.example.playlistmaker.search.domain.model.Track

sealed class PlaylistScreenState(open val playlist: Playlist) {
    class Content(
        val tracks: List<Track>,
        val durationMin: Long,
        override val playlist: Playlist
    ): PlaylistScreenState(playlist)
    class Empty(
        override val playlist: Playlist
    ): PlaylistScreenState(playlist)
}
