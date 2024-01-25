package com.example.playlistmaker.domain.api

import com.example.playlistmaker.domain.model.Track

interface SaveTrackToCacheUseCase {
    fun execute(track: Track)
}