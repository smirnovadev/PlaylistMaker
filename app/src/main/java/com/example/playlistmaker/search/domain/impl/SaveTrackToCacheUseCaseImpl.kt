package com.example.playlistmaker.search.domain.impl

import com.example.playlistmaker.player.domain.TrackCacheRepository
import com.example.playlistmaker.search.domain.api.SaveTrackToCacheUseCase
import com.example.playlistmaker.search.domain.model.Track

class SaveTrackToCacheUseCaseImpl(
    private val repository: TrackCacheRepository
): SaveTrackToCacheUseCase {
    override fun execute(track: Track) {
        repository.saveTrack(track)
    }

}