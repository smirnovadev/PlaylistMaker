package com.example.playlistmaker.search.data

import com.example.playlistmaker.search.data.dto.TrackResponse
import com.example.playlistmaker.search.data.dto.TrackSearchRequest
import com.example.playlistmaker.search.data.mapper.TrackMapper
import com.example.playlistmaker.search.data.network.RetrofitNetworkClient
import com.example.playlistmaker.search.domain.api.SearchTrackRepository
import com.example.playlistmaker.search.domain.model.Track
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class SearchTrackRepositoryImpl(
    private val networkClient: RetrofitNetworkClient,
    private val trackMapper: TrackMapper
) : SearchTrackRepository {
    override suspend fun getTrackList(query: String): List<Track>? {
        return suspendCoroutine { continuation ->
            val trackSearchRequest = TrackSearchRequest(query)
            val response = networkClient.doRequest(trackSearchRequest)
            if (response.resultCode == 200) {
                val trackResponse = response as TrackResponse
                continuation.resume(trackResponse.results.map { dto ->
                    trackMapper.map(dto)
                })
            } else {
                continuation.resume(null)
            }
        }
    }
}