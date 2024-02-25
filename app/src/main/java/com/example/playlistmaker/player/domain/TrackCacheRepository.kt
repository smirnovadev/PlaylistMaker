package com.example.playlistmaker.player.domain

import com.example.playlistmaker.search.domain.model.Track

interface TrackCacheRepository {
    fun getTrack() : Track?
    fun saveTrack(track: Track)

}