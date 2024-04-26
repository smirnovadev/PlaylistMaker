package com.example.playlistmaker.player.ui

data class PlayerState(
    val timeMillis: Int,
    val isPlaying: Boolean,
    val isPrepared: Boolean,
)