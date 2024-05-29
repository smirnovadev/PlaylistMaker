package com.example.playlistmaker.db.entity.domain.impl

import com.example.playlistmaker.createPlaylist.domain.model.Playlist
import com.example.playlistmaker.db.entity.domain.GetPlaylistByIdUseCase
import com.example.playlistmaker.db.entity.domain.PlaylistRepository
import kotlinx.coroutines.flow.Flow

class GetPlaylistByIdUseCaseImpl(private val playlistRepository: PlaylistRepository): GetPlaylistByIdUseCase {
    override suspend fun execute(playlistId: Long): Flow<Playlist> {
        return playlistRepository.getPlaylistById(playlistId)
    }
}
