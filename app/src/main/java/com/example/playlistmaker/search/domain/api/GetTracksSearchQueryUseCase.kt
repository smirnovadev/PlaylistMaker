package com.example.playlistmaker.search.domain.api

import com.example.playlistmaker.search.domain.model.Track

interface GetTracksSearchQueryUseCase {
    suspend fun execute(query: String): List<Track>?
}