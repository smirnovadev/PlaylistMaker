package com.example.playlistmaker.search.domain.api

import com.example.playlistmaker.search.domain.model.Track
import kotlinx.coroutines.flow.Flow

interface SearchTrackRepository {
    fun getTrackList(query: String): Flow<List<Track>?>
}
