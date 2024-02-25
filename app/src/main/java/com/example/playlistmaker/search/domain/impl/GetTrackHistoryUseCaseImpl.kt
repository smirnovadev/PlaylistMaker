package com.example.playlistmaker.search.domain.impl

import com.example.playlistmaker.search.domain.api.GetTrackHistoryUseCase
import com.example.playlistmaker.search.domain.api.SearchHistoryRepository
import com.example.playlistmaker.search.domain.model.Track

class GetTrackHistoryUseCaseImpl(
    private val repository: SearchHistoryRepository
): GetTrackHistoryUseCase {
    override fun execute(): List<Track> {
        return repository.getTrackHistoryList().reversed()
    }
}