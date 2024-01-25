package com.example.playlistmaker.domain.impl

import com.example.playlistmaker.domain.api.GetTrackFromCacheUseCase
import com.example.playlistmaker.domain.api.TrackCacheRepository
import com.example.playlistmaker.domain.model.Track

class GetTrackFromCacheUseCaseImpl (private val trackCacheRepository: TrackCacheRepository): GetTrackFromCacheUseCase {
    override fun execute(): Track? {
        return trackCacheRepository.getTrack()
    }
}