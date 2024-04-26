package com.example.playlistmaker.search.domain.impl

import com.example.playlistmaker.search.domain.api.GetTracksSearchQueryUseCase
import com.example.playlistmaker.search.domain.api.SearchTrackRepository
import com.example.playlistmaker.search.domain.model.Track
import kotlinx.coroutines.flow.Flow

class GetTracksSearchQueryUseCaseImpl(
    private val repository : SearchTrackRepository
): GetTracksSearchQueryUseCase {
    override suspend fun execute(query: String): Flow<List<Track>?> {
       return repository.getTrackList(query)
    }
}
