package com.example.playlistmaker.db.entity.domain.db

import com.example.playlistmaker.search.domain.model.Track

interface ClearFavoriteTrackUseCase {
    suspend fun execute(track: Track)
}