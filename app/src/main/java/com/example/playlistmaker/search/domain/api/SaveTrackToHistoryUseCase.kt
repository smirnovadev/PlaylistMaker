package com.example.playlistmaker.search.domain.api

import com.example.playlistmaker.search.domain.model.Track
import kotlinx.coroutines.flow.Flow

interface SaveTrackToHistoryUseCase {
    fun execute(track: Track): Flow<List<Track>>
}