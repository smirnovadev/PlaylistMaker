package com.example.playlistmaker.domain.impl

import com.example.playlistmaker.domain.api.SaveTrackToHistoryUseCase
import com.example.playlistmaker.domain.api.SearchHistoryRepository
import com.example.playlistmaker.domain.model.Track

class SaveTrackToHistoryUseCaseImpl(
    private val repository: SearchHistoryRepository
): SaveTrackToHistoryUseCase {
    override fun execute(track: Track): List<Track> {
        val listTrack = repository.getTrackHistoryList()
        listTrack.removeAll { it.trackId == track.trackId }
        listTrack.add(track)

        if (listTrack.size > MAX_TRACKS) {
            listTrack.removeAt(0)
        }

        repository.saveTrackHistoryList(listTrack)
        return listTrack
    }
    companion object {
        private const val MAX_TRACKS = 10
    }
}