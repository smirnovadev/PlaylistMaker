package com.example.playlistmaker.search.domain.impl

import com.example.playlistmaker.search.domain.api.ClearTrackHistoryUseCase
import com.example.playlistmaker.search.domain.api.SearchHistoryRepository

class ClearTrackHistoryUseCaseImpl(
    private val repository: SearchHistoryRepository
): ClearTrackHistoryUseCase {
    override fun execute() {
        repository.clearHistory()
    }
}