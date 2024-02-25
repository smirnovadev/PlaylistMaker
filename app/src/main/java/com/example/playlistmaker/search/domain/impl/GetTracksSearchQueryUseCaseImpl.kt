package com.example.playlistmaker.search.domain.impl

import com.example.playlistmaker.search.domain.api.GetTracksSearchQueryUseCase
import com.example.playlistmaker.search.domain.api.SearchTrackRepository
import com.example.playlistmaker.search.domain.model.Track

class GetTracksSearchQueryUseCaseImpl(
    private val repository : SearchTrackRepository
): GetTracksSearchQueryUseCase {
    override suspend fun execute(query: String): List<Track>? {
       return repository.getTrackList(query)
    }
}


