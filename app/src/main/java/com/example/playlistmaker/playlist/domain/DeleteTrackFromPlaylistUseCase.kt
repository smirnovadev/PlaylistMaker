package com.example.playlistmaker.playlist.domain

import com.example.playlistmaker.createPlaylist.domain.model.Playlist
import kotlinx.coroutines.flow.Flow

interface DeleteTrackFromPlaylistUseCase {
    suspend fun execute(trackId: Long, playlist: Playlist): Flow<Playlist>
}