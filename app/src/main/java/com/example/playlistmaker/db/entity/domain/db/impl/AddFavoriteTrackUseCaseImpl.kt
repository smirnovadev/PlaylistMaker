package com.example.playlistmaker.db.entity.domain.db.impl

import com.example.playlistmaker.db.entity.domain.db.AddFavoriteTrackUseCase
import com.example.playlistmaker.db.entity.domain.db.FavoritesTrackRepository
import com.example.playlistmaker.search.domain.model.Track

class AddFavoriteTrackUseCaseImpl(private val favoritesTrackRepository: FavoritesTrackRepository)
    : AddFavoriteTrackUseCase {
    override suspend fun execute(track: Track) {
        return favoritesTrackRepository.addFavoriteTrack(track)
    }
}