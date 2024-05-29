package com.example.playlistmaker.playlist.domain.impl

import com.example.playlistmaker.db.entity.domain.PlaylistRepository
import com.example.playlistmaker.playlist.domain.GetAllTracksForPlaylistUseCase
import com.example.playlistmaker.search.domain.model.Track
import kotlinx.coroutines.flow.Flow

class GetAllTracksForPlaylistUseCaseImpl(private val playListRepository: PlaylistRepository)
    : GetAllTracksForPlaylistUseCase {
    override suspend fun execute(playlistTrackIds: List<Long>): Flow<List<Track>> {
        return playListRepository.getAllTracksForPlaylist(playlistTrackIds)
    }
}