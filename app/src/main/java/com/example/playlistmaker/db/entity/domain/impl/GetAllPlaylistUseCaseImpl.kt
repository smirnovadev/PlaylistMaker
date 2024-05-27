package com.example.playlistmaker.db.entity.domain.impl

import com.example.playlistmaker.db.entity.domain.GetAllPlaylistUseCase
import com.example.playlistmaker.db.entity.domain.PlaylistRepository
import com.example.playlistmaker.playlist.domain.model.Playlist
import kotlinx.coroutines.flow.Flow

class GetAllPlaylistUseCaseImpl(private val playlistRepository: PlaylistRepository): GetAllPlaylistUseCase {
    override suspend fun execute(): Flow<List<Playlist>> {
        return playlistRepository.getAllPlaylist()
    }
}