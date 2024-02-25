package com.example.playlistmaker.domain.api

import com.example.playlistmaker.domain.model.Track

interface TrackCacheRepository {
    fun getTrack() : Track?
    fun saveTrack(track: Track)

}