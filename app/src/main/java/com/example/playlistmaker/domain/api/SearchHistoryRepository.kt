package com.example.playlistmaker.domain.api

import com.example.playlistmaker.domain.model.Track

interface SearchHistoryRepository {
    fun clearHistory()

    fun saveTrackHistoryList(listTrack: MutableList<Track>)

    fun getTrackHistoryList(): MutableList<Track>
}