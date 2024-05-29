package com.example.playlistmaker.playlist.domain.impl

import com.example.playlistmaker.createPlaylist.domain.model.Playlist
import com.example.playlistmaker.db.entity.domain.PlaylistRepository
import com.example.playlistmaker.playlist.domain.DeletePlaylistUseCase
import kotlinx.coroutines.flow.Flow

class DeletePlaylistUseCaseImpl(
    private val  playlistRepository: PlaylistRepository
): DeletePlaylistUseCase {
    override suspend fun execute(playlist: Playlist): Flow<Unit> {
        return playlistRepository.deletePlaylist(playlist)
    }
}