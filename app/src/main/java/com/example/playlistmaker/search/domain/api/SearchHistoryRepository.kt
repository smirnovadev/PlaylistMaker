package com.example.playlistmaker.search.domain.api

import com.example.playlistmaker.search.domain.model.Track

interface SearchHistoryRepository {
    fun clearHistory()

    fun saveTrackHistoryList(listTrack: MutableList<Track>)

    fun getTrackHistoryList(): MutableList<Track>
}