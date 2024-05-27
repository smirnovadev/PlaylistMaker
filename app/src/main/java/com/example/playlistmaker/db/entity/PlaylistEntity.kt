package com.example.playlistmaker.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters

@Entity(tableName = "playlist_table")
@TypeConverters(PlaylistConverter::class)
data class PlaylistEntity(
    @PrimaryKey(autoGenerate = true)
    val playlistId: Long = 0L,
    val playlistName: String,
    val descriptionPlaylist: String,
    val coverArtPath: String,
    val tracksId: String,
    val numberTracks: Int
)