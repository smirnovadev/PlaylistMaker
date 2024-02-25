package com.example.playlistmaker.domain.api

import com.example.playlistmaker.domain.model.Track

interface GetTracksSearchQueryUseCase {
    suspend fun execute(query: String): List<Track>?
}