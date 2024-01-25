package com.example.playlistmaker.domain.impl

import com.example.playlistmaker.domain.api.ClearTrackHistoryUseCase
import com.example.playlistmaker.domain.api.SearchHistoryRepository

class ClearTrackHistoryUseCaseImpl(
    private val repository: SearchHistoryRepository
): ClearTrackHistoryUseCase {
    override fun execute() {
        repository.clearHistory()
    }
}