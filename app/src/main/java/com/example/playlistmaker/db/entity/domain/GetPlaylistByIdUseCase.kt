package com.example.playlistmaker.db.entity.domain

import com.example.playlistmaker.createPlaylist.domain.model.Playlist
import kotlinx.coroutines.flow.Flow

interface GetPlaylistByIdUseCase {
    suspend fun execute(playlistId: Long): Flow<Playlist>
}