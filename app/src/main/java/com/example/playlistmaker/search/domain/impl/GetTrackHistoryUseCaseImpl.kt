package com.example.playlistmaker.search.domain.impl

import com.example.playlistmaker.search.domain.api.GetTrackHistoryUseCase
import com.example.playlistmaker.search.domain.api.SearchHistoryRepository
import com.example.playlistmaker.search.domain.model.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetTrackHistoryUseCaseImpl(
    private val repository: SearchHistoryRepository
): GetTrackHistoryUseCase {
    override fun execute(): Flow<List<Track>> {
        return repository.getTrackHistoryList().map {
            it.reversed()
        }
    }
}