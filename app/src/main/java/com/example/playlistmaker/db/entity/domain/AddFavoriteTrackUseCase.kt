package com.example.playlistmaker.db.entity.domain

import com.example.playlistmaker.search.domain.model.Track

interface AddFavoriteTrackUseCase {
    suspend fun execute(track: Track)
}