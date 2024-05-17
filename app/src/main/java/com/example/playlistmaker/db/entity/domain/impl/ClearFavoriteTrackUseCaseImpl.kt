package com.example.playlistmaker.db.entity.domain.impl

import com.example.playlistmaker.db.entity.domain.ClearFavoriteTrackUseCase
import com.example.playlistmaker.db.entity.domain.FavoritesTrackRepository
import com.example.playlistmaker.search.domain.model.Track

class ClearFavoriteTrackUseCaseImpl(private val favoritesTrackRepository: FavoritesTrackRepository)
    : ClearFavoriteTrackUseCase {
    override suspend fun execute(track: Track) {
        return favoritesTrackRepository.clearFavoriteTrack(track)
    }
}