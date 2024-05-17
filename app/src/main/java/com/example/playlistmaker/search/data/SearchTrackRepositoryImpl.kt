package com.example.playlistmaker.search.data

import com.example.playlistmaker.db.entity.AppDatabase
import com.example.playlistmaker.search.data.dto.TrackResponse
import com.example.playlistmaker.search.data.dto.TrackSearchRequest
import com.example.playlistmaker.search.data.mapper.TrackMapper
import com.example.playlistmaker.search.domain.api.SearchTrackRepository
import com.example.playlistmaker.search.domain.model.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class SearchTrackRepositoryImpl(
    private val networkClient: NetworkClient,
    private val trackMapper: TrackMapper,
    private val appDatabase: AppDatabase,
) : SearchTrackRepository {
    override fun getTrackList(query: String): Flow<List<Track>?> = flow {
        val trackSearchRequest = TrackSearchRequest(query)
        val response = networkClient.doRequest(trackSearchRequest)
        if (response.resultCode == 200) {
            val trackResponse = response as TrackResponse

            val resultTracks = trackResponse.results.map { dto ->
                trackMapper.map(dto)
            }
            val favoritesTracks = appDatabase.trackDao().getAllTrackIds()

            resultTracks.forEach { track ->
                if (favoritesTracks.contains(track.trackId)) {
                    track.isFavorite = true
                }
            }
            emit(resultTracks)
        } else {
            emit(null)
        }
    }
}