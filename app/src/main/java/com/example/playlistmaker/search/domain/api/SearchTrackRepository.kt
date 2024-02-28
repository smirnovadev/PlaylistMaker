package com.example.playlistmaker.search.domain.api

import com.example.playlistmaker.search.domain.model.Track

interface SearchTrackRepository {
    suspend fun getTrackList(query: String): List<Track>?
}