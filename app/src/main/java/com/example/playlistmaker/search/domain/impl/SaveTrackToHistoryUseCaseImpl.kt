package com.example.playlistmaker.search.domain.impl

import com.example.playlistmaker.search.domain.api.SaveTrackToHistoryUseCase
import com.example.playlistmaker.search.domain.api.SearchHistoryRepository
import com.example.playlistmaker.search.domain.model.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map


class SaveTrackToHistoryUseCaseImpl(
    private val repository: SearchHistoryRepository
) : SaveTrackToHistoryUseCase {
    override fun execute(track: Track): Flow<List<Track>> {
        return repository.getTrackHistoryList().map { currentList ->
            val mutableTrackList = currentList.toMutableList().apply {
                removeAll { it.trackId == track.trackId }
                add(track)
                if (size > MAX_TRACKS) {
                    removeAt(0)
                }
            }
            repository.saveTrackHistoryList(mutableTrackList)
            mutableTrackList
        }
    }
    companion object {
        private const val MAX_TRACKS = 10
    }
}