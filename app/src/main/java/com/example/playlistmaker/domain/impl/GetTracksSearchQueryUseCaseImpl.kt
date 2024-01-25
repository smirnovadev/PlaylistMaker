package com.example.playlistmaker.domain.impl

import com.example.playlistmaker.domain.api.GetTracksSearchQueryUseCase
import com.example.playlistmaker.domain.api.SearchTrackRepository
import com.example.playlistmaker.domain.model.Track

class GetTracksSearchQueryUseCaseImpl(
    private val repository : SearchTrackRepository
): GetTracksSearchQueryUseCase{
    override suspend fun execute(query: String): List<Track>? {
       return repository.getTrackList(query)
    }
}


