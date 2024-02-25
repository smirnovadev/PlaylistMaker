package com.example.playlistmaker.player.domain

import com.example.playlistmaker.search.domain.model.Track

interface GetTrackFromCacheUseCase {
    fun execute(): Track?
}