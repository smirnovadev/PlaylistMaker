package com.example.playlistmaker.db.entity.data.db

import com.example.playlistmaker.db.entity.AppDatabase
import com.example.playlistmaker.db.entity.PlaylistConverter
import com.example.playlistmaker.db.entity.PlaylistEntity
import com.example.playlistmaker.db.entity.TrackDbConvertor
import com.example.playlistmaker.db.entity.domain.PlaylistRepository
import com.example.playlistmaker.playlist.domain.model.Playlist
import com.example.playlistmaker.search.domain.model.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class PlaylistRepositoryImpl
    (
    private val appDataBase: AppDatabase,
    private val playlistDbConverter: PlaylistConverter,
    private val trackDbConvertor: TrackDbConvertor,
) : PlaylistRepository {
    override suspend fun addPlaylist(playlist: Playlist) {
        appDataBase.playlistDao().insertPlaylist(playlistDbConverter.map(playlist))
    }

    override suspend fun getAllPlaylist(): Flow<List<Playlist>> = flow {
        val playlist = appDataBase.playlistDao().getAllPlaylist()
        emit(convertFromPlaylistEntity(playlist))
    }

    override suspend fun addTrackForPlaylist(track: Track, playlist: Playlist) {
        val trackIds = playlist.tracksId.toMutableList()
        trackIds.add(track.trackId)
        val newPlaylist = playlist.copy(
            tracksId = trackIds,
            numberTracks = playlist.numberTracks + 1
        )
        appDataBase.playlistDao().insertPlaylist(playlistDbConverter.map(newPlaylist))
        appDataBase.tracksForPlaylistDao()
            .insertTracksForPlaylist(trackDbConvertor.mapToTracksForPlaylistEntity(track))
    }

    private fun convertFromPlaylistEntity(playlist: List<PlaylistEntity>): List<Playlist> {
        return playlist.map { playlist ->
            playlistDbConverter.map(playlist)
        }
    }
}




