package com.example.playlistmaker.db.entity.domain.impl

import com.example.playlistmaker.createPlaylist.domain.model.Playlist
import com.example.playlistmaker.db.entity.domain.GetAllPlaylistUseCase
import com.example.playlistmaker.db.entity.domain.PlaylistRepository
import kotlinx.coroutines.flow.Flow

class GetAllPlaylistUseCaseImpl(private val playlistRepository: PlaylistRepository): GetAllPlaylistUseCase {
    override suspend fun execute(): Flow<List<Playlist>> {
        return playlistRepository.getAllPlaylist()
    }
}