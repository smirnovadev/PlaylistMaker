package com.example.playlistmaker.db.entity.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.playlistmaker.db.entity.TracksForPlaylistEntity

@Dao
interface TracksForPlaylistDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertTracksForPlaylist(tracksForPlaylistEntity: TracksForPlaylistEntity)
    @Query("SELECT * FROM tracks_for_playlist_table")
    suspend fun getAllTracksForPlaylist(): List<TracksForPlaylistEntity>
    @Delete(entity = TracksForPlaylistEntity::class)
    suspend fun deleteTrack(byTrackId: ByTrackId)
}

data class ByTrackId(val trackId: Long)