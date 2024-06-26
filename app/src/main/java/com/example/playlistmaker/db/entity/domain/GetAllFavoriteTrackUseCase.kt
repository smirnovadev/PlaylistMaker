package com.example.playlistmaker.db.entity.domain

import com.example.playlistmaker.search.domain.model.Track
import kotlinx.coroutines.flow.Flow

interface GetAllFavoriteTrackUseCase {
    suspend fun execute(): Flow<List<Track>>
}