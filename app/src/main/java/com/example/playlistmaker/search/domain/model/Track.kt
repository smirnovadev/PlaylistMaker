package com.example.playlistmaker.search.domain.model

import java.io.Serializable


data class Track(
    val previewUrl: String?,
    val trackId: Int,
    val trackName: String,
    val artistName: String,
    val formattedDuration: String,
    val artworkUrl100: String,
    val artworkUrl512: String,
    val collectionName: String?,
    val releaseYear: String,
    val primaryGenreName: String,
    val country: String
) : Serializable