package com.example.playlistmaker.playlist.domain.impl

import com.example.playlistmaker.createPlaylist.domain.model.Playlist
import com.example.playlistmaker.db.entity.domain.PlaylistRepository
import com.example.playlistmaker.playlist.domain.DeleteTrackFromPlaylistUseCase
import kotlinx.coroutines.flow.Flow

class DeleteTrackFromPlaylistUseCaseImpl(
    private val playlistRepository: PlaylistRepository
) : DeleteTrackFromPlaylistUseCase {
    override suspend fun execute(trackId: Long, playlist: Playlist): Flow<Playlist> {
        return playlistRepository.deleteTrackFromPlaylist(trackId, playlist)
    }
}