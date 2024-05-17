package com.example.playlistmaker.db.entity.domain.db.impl

import com.example.playlistmaker.db.entity.domain.db.ClearFavoriteTrackUseCase
import com.example.playlistmaker.db.entity.domain.db.FavoritesTrackRepository
import com.example.playlistmaker.search.domain.model.Track

class ClearFavoriteTrackUseCaseImpl(private val favoritesTrackRepository: FavoritesTrackRepository)
    : ClearFavoriteTrackUseCase {
    override suspend fun execute(track: Track) {
        return favoritesTrackRepository.clearFavoriteTrack(track)
    }
}