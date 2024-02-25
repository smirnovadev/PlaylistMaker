package com.example.playlistmaker.domain.api

import com.example.playlistmaker.domain.model.Track

interface SaveTrackToHistoryUseCase {
    fun execute(track: Track): List<Track>
}