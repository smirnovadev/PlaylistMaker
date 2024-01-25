package com.example.playlistmaker.domain.impl

import com.example.playlistmaker.domain.api.SaveTrackToCacheUseCase
import com.example.playlistmaker.domain.api.TrackCacheRepository
import com.example.playlistmaker.domain.model.Track

class SaveTrackToCacheUseCaseImpl(
    private val repository: TrackCacheRepository
): SaveTrackToCacheUseCase {
    override fun execute(track: Track) {
        repository.saveTrack(track)
    }

}