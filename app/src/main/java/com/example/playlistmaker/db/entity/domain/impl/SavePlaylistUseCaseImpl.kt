package com.example.playlistmaker.db.entity.domain.impl

import com.example.playlistmaker.db.entity.domain.PlaylistRepository
import com.example.playlistmaker.db.entity.domain.SavePlaylistUseCase
import com.example.playlistmaker.playlist.domain.model.Playlist

class SavePlaylistUseCaseImpl(private val playlistRepository: PlaylistRepository): SavePlaylistUseCase {
    override suspend fun execute(playlist: Playlist) {
        playlistRepository.addPlaylist(playlist)
    }
}