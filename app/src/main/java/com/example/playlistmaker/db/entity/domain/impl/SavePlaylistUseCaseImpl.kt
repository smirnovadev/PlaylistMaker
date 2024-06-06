package com.example.playlistmaker.db.entity.domain.impl

import com.example.playlistmaker.createPlaylist.domain.model.Playlist
import com.example.playlistmaker.db.entity.domain.PlaylistRepository
import com.example.playlistmaker.db.entity.domain.SavePlaylistUseCase

class SavePlaylistUseCaseImpl(private val playlistRepository: PlaylistRepository): SavePlaylistUseCase {
    override suspend fun execute(playlist: Playlist) {
        playlistRepository.addPlaylist(playlist)
    }
}