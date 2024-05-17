package com.example.playlistmaker.db.entity.data.db

import com.example.playlistmaker.db.entity.AppDatabase
import com.example.playlistmaker.db.entity.TrackDbConvertor
import com.example.playlistmaker.db.entity.TrackEntity
import com.example.playlistmaker.db.entity.domain.db.FavoritesTrackRepository
import com.example.playlistmaker.search.domain.model.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FavoritesTrackRepositoryImpl(
    private val appDataBase: AppDatabase,
    private val trackDbConverter: TrackDbConvertor
) : FavoritesTrackRepository {
    override suspend fun addFavoriteTrack(track: Track) {
        appDataBase.trackDao().insertTrack(trackDbConverter.map(track))
    }

    override suspend fun clearFavoriteTrack(track: Track) {
        appDataBase.trackDao().deleteTrack(trackDbConverter.map(track))
    }

    override suspend fun getAllFavoriteTrack(): Flow<List<Track>> = flow {
        val tracks = appDataBase.trackDao().getTracks()
        emit(convertFromTrackEntity(tracks))

    }

    private fun convertFromTrackEntity(tracks: List<TrackEntity>): List<Track> {
        return tracks.map { track ->
            trackDbConverter.map(track)
        }
    }
}
