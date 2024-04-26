package com.example.playlistmaker.db.entity.domain.db.impl

import com.example.playlistmaker.db.entity.domain.db.FavoritesTrackRepository
import com.example.playlistmaker.db.entity.domain.db.GetAllFavoriteTrackUseCase
import com.example.playlistmaker.search.domain.model.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetAllFavoriteTrackUseCaseImpl(private val favoritesTrackRepository: FavoritesTrackRepository)
    : GetAllFavoriteTrackUseCase {
    override suspend fun execute(): Flow<List<Track>> {
        return favoritesTrackRepository.getAllFavoriteTrack()
            .map { tracks ->
                tracks.forEach{
                    track ->  track.isFavorite = true
                }
                tracks
            }
    }
}