package com.example.playlistmaker.db.entity.data.db

import com.example.playlistmaker.createPlaylist.domain.model.Playlist
import com.example.playlistmaker.db.entity.AppDatabase
import com.example.playlistmaker.db.entity.PlaylistConverter
import com.example.playlistmaker.db.entity.PlaylistEntity
import com.example.playlistmaker.db.entity.TrackDbConvertor
import com.example.playlistmaker.db.entity.dao.ByTrackId
import com.example.playlistmaker.db.entity.domain.PlaylistRepository
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
        val trackIds = playlist.trackIdList.toMutableList()
        trackIds.add(0, track.trackId)
        val newPlaylist = playlist.copy(
            trackIdList = trackIds,
            numberTracks = playlist.numberTracks + 1
        )
        appDataBase.playlistDao().insertPlaylist(playlistDbConverter.map(newPlaylist))
        appDataBase.tracksForPlaylistDao()
            .insertTracksForPlaylist(trackDbConvertor.mapToTracksForPlaylistEntity(track))
    }

    override suspend fun getPlaylistById(playlistId: Long): Flow<Playlist> = flow {
        val playlistById = appDataBase.playlistDao().getPlaylistById(playlistId)
        emit(playlistDbConverter.map(playlistById))
    }

    override suspend fun getAllTracksForPlaylist(playlistTrackIds: List<Long>): Flow<List<Track>> =
        flow {
            val tracks = appDataBase.tracksForPlaylistDao()
                .getAllTracksForPlaylist()
                .filter { playlistTrackIds.contains(it.trackId) }
                .sortedBy { playlistTrackIds.indexOf(it.trackId) }
                .map { trackDbConvertor.mapToTrack(it) }

            val favoritesTracksId = appDataBase.trackDao().getAllTrackIds()

            tracks.forEach { track ->
                if (favoritesTracksId.contains(track.trackId)) {
                    track.isFavorite = true
                }
            }
            emit(tracks)
        }

    override suspend fun deleteTrackFromPlaylist(
        trackId: Long,
        playlist: Playlist
    ): Flow<Playlist> = flow {
        val newTrackId = playlist.trackIdList.toMutableList()
        newTrackId.remove(trackId)
        val newPlaylist = playlist.copy(
            trackIdList = newTrackId,
            numberTracks = playlist.numberTracks - 1
        )
        appDataBase.playlistDao().insertPlaylist(playlistDbConverter.map(newPlaylist))
        val allPlaylists = appDataBase.playlistDao().getAllPlaylist()
        var delete = true
        allPlaylists.forEach { playlistEntity ->
            val tracks = playlistDbConverter.toTrackIdList(playlistEntity.trackIdList)
            if (tracks.contains(trackId)) {
                delete = false
            }
        }
        if (delete) {
            appDataBase.tracksForPlaylistDao().deleteTrack(ByTrackId(trackId))
        }
        emit(newPlaylist)
    }

    override suspend fun deletePlaylist(playlist: Playlist): Flow<Unit> = flow {
        appDataBase.playlistDao().deletePlaylist(playlistDbConverter.map(playlist))
        val tracksToDelete = playlist.trackIdList
        val allPlaylists = appDataBase.playlistDao().getAllPlaylist()
        for (track in tracksToDelete) {
            var delete = true
            for (playlistEntity in allPlaylists) {
                val tracks = playlistDbConverter.toTrackIdList(playlistEntity.trackIdList)
                if (tracks.contains(track)) {
                    delete = false
                }
            }
            if (delete) {
                appDataBase.tracksForPlaylistDao().deleteTrack(ByTrackId(track))
            }
        }
    }

    private fun convertFromPlaylistEntity(playlists: List<PlaylistEntity>): List<Playlist> {
        return playlists.map { playlist ->
            playlistDbConverter.map(playlist)
        }
    }
}




