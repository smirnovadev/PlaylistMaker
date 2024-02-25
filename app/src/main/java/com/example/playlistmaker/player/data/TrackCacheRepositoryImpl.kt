package com.example.playlistmaker.player.data

import com.example.playlistmaker.player.domain.TrackCacheRepository
import com.example.playlistmaker.search.domain.model.Track

class TrackCacheRepositoryImpl : TrackCacheRepository {

    private var savedTrack: Track? = null
   override fun getTrack(): Track? {
        return savedTrack
    }
    override fun saveTrack(track: Track) {
        savedTrack = track
    }
}