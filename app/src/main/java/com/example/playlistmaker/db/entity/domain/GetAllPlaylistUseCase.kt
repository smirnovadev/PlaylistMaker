package com.example.playlistmaker.db.entity.domain

import com.example.playlistmaker.createPlaylist.domain.model.Playlist
import kotlinx.coroutines.flow.Flow

interface GetAllPlaylistUseCase {
    suspend fun execute(): Flow<List<Playlist>>
}