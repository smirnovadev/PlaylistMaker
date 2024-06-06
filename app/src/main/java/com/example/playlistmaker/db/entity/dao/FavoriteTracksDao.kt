package com.example.playlistmaker.db.entity.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.playlistmaker.db.entity.FavoriteTrackEntity

@Dao
interface FavoriteTracksDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTrack(tracks: FavoriteTrackEntity)

    @Delete
    suspend fun deleteTrack(favoriteTrackEntity: FavoriteTrackEntity)

    @Query( "SELECT * FROM track_table ORDER BY createdTime DESC")
    suspend fun getTracks(): List<FavoriteTrackEntity>

    @Query("SELECT trackId FROM track_table  ")
    suspend fun getAllTrackIds(): List<Long>
}