package com.example.playlistmaker.playlist.domain

import com.example.playlistmaker.search.domain.model.Track
import kotlinx.coroutines.flow.Flow

interface GetAllTracksForPlaylistUseCase {
    suspend fun execute(playlistTrackIds: List<Long>) : Flow<List<Track>>
}