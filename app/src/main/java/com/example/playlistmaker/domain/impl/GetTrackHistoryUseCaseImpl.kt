package com.example.playlistmaker.domain.impl

import com.example.playlistmaker.domain.api.GetTrackHistoryUseCase
import com.example.playlistmaker.domain.api.SearchHistoryRepository
import com.example.playlistmaker.domain.model.Track

class GetTrackHistoryUseCaseImpl(
    private val repository: SearchHistoryRepository
): GetTrackHistoryUseCase {
    override fun execute(): List<Track> {
        return repository.getTrackHistoryList().reversed()
    }
}