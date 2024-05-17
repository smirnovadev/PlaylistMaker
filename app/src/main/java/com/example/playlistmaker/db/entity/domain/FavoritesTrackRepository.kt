package com.example.playlistmaker.db.entity.domain

import com.example.playlistmaker.search.domain.model.Track
import kotlinx.coroutines.flow.Flow

interface FavoritesTrackRepository {
    suspend fun addFavoriteTrack(track: Track)

    suspend fun clearFavoriteTrack(track: Track)

    suspend fun getAllFavoriteTrack(): Flow<List<Track>>

}