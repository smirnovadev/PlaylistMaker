package com.example.playlistmaker.db.entity.domain

import com.example.playlistmaker.playlist.domain.model.Playlist

interface SavePlaylistUseCase {
    suspend fun execute(playlist: Playlist)
}