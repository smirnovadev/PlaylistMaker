package com.example.playlistmaker.db.entity.domain.impl

import com.example.playlistmaker.db.entity.domain.AddFavoriteTrackUseCase
import com.example.playlistmaker.db.entity.domain.FavoritesTrackRepository
import com.example.playlistmaker.search.domain.model.Track


class AddFavoriteTrackUseCaseImpl(private val favoritesTrackRepository: FavoritesTrackRepository)
    : AddFavoriteTrackUseCase {
    override suspend fun execute(track: Track) {
        return favoritesTrackRepository.addFavoriteTrack(track)
    }
}