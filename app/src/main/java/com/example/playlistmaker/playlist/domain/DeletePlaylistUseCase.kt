package com.example.playlistmaker.playlist.domain

import com.example.playlistmaker.createPlaylist.domain.model.Playlist
import kotlinx.coroutines.flow.Flow

interface DeletePlaylistUseCase {
    suspend fun execute(playlist: Playlist) : Flow<Unit>
}