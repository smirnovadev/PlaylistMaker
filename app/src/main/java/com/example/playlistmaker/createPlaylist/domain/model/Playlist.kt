package com.example.playlistmaker.createPlaylist.domain.model

import java.io.Serializable

data class Playlist(
    val playlistId: Long = 0L,
    val playlistName: String,
    val description: String,
    val coverArtPath: String,
    val trackIdList: List<Long>,
    val numberTracks: Int
): Serializable
