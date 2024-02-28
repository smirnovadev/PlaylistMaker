package com.example.playlistmaker.player.domain

import com.example.playlistmaker.search.domain.model.Track

class GetTrackFromCacheUseCaseImpl (private val trackCacheRepository: TrackCacheRepository):
    GetTrackFromCacheUseCase {
    override fun execute(): Track? {
        return trackCacheRepository.getTrack()
    }
}