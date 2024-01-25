package com.example.playlistmaker.data

import com.example.playlistmaker.domain.api.TrackCacheRepository
import com.example.playlistmaker.domain.model.Track

class TrackCacheRepositoryImpl(): TrackCacheRepository {

    private var savedTrack: Track? = null
   override fun getTrack(): Track? {
        return savedTrack
    }
    override fun saveTrack(track: Track) {
        savedTrack = track
    }
}