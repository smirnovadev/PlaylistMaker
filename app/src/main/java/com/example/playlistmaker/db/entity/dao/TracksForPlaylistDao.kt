package com.example.playlistmaker.db.entity.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import com.example.playlistmaker.db.entity.TracksForPlaylistEntity

@Dao
interface TracksForPlaylistDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertTracksForPlaylist(tracksForPlaylistEntity: TracksForPlaylistEntity)
}