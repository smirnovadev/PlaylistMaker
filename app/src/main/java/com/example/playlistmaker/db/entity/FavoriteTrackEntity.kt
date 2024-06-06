package com.example.playlistmaker.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "track_table")
data class FavoriteTrackEntity(
    @PrimaryKey
    val trackId: Long,
    val previewUrl: String?,
    val trackName: String,
    val artistName: String,
    val formattedDuration: String,
    val artworkUrl100: String,
    val artworkUrl512: String,
    val collectionName: String?,
    val releaseYear: String,
    val primaryGenreName: String,
    val country: String,
    val createdTime: Long,
)