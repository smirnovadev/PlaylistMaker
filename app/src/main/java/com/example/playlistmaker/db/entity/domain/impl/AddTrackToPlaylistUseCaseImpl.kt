package com.example.playlistmaker.db.entity.domain.impl

import com.example.playlistmaker.createPlaylist.domain.model.Playlist
import com.example.playlistmaker.db.entity.domain.AddTrackToPlaylistUseCase
import com.example.playlistmaker.db.entity.domain.PlaylistRepository
import com.example.playlistmaker.search.domain.model.Track

class AddTrackToPlaylistUseCaseImpl(private val playlistRepository: PlaylistRepository): AddTrackToPlaylistUseCase {
    override suspend fun execute(track: Track, playlist: Playlist) {
        playlistRepository.addTrackForPlaylist(track, playlist)
    }
}