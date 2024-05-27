package com.example.playlistmaker.db.entity.domain

import com.example.playlistmaker.playlist.domain.model.Playlist
import com.example.playlistmaker.search.domain.model.Track

interface AddTrackToPlaylistUseCase {
    suspend fun execute(track: Track, playlist: Playlist)
}