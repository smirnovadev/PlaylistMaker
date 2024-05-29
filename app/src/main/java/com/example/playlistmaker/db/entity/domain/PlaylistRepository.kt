package com.example.playlistmaker.db.entity.domain

import com.example.playlistmaker.createPlaylist.domain.model.Playlist
import com.example.playlistmaker.search.domain.model.Track
import kotlinx.coroutines.flow.Flow

interface PlaylistRepository {
    suspend fun addPlaylist(playlist: Playlist)
    suspend fun getAllPlaylist(): Flow<List<Playlist>>
    suspend fun addTrackForPlaylist(track: Track, playlist: Playlist)

    suspend fun getPlaylistById(playlistId: Long) : Flow<Playlist>

    suspend fun getAllTracksForPlaylist(playlistTrackIds: List<Long>):Flow<List<Track>>

    suspend fun deleteTrackFromPlaylist(trackId: Long, playlist: Playlist): Flow<Playlist>

    suspend fun deletePlaylist(playlist: Playlist, ): Flow<Unit>

}