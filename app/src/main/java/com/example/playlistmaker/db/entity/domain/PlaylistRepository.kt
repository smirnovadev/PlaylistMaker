package com.example.playlistmaker.db.entity.domain

import com.example.playlistmaker.playlist.domain.model.Playlist
import com.example.playlistmaker.search.domain.model.Track
import kotlinx.coroutines.flow.Flow

interface PlaylistRepository {
    suspend fun addPlaylist(playlist: Playlist)
    suspend fun getAllPlaylist(): Flow<List<Playlist>>
    suspend fun addTrackForPlaylist(track: Track, playlist: Playlist)

}