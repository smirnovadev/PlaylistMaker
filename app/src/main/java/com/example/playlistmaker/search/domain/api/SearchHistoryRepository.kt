package com.example.playlistmaker.search.domain.api

import com.example.playlistmaker.search.domain.model.Track
import kotlinx.coroutines.flow.Flow

interface SearchHistoryRepository {
    fun clearHistory()

    fun saveTrackHistoryList(listTrack: MutableList<Track>)

     fun getTrackHistoryList(): Flow<MutableList<Track>>
}