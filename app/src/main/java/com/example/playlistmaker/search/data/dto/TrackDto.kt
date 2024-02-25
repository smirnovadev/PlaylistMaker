package com.example.playlistmaker.search.data.dto

import java.io.Serializable

data class TrackDto(
    val previewUrl: String?,
    val trackId: Int,
    val trackName: String,
    val artistName: String,
    val trackTimeMillis: Int,
    val artworkUrl100: String,
    val collectionName: String?,
    val releaseDate: String,
    val primaryGenreName: String,
    val country: String
) : Serializable