package com.example.playlistmaker.data

import com.example.playlistmaker.data.dto.TrackResponse
import com.example.playlistmaker.data.dto.TrackSearchRequest
import com.example.playlistmaker.data.mapper.TrackMapper
import com.example.playlistmaker.data.network.RetrofitNetworkClient
import com.example.playlistmaker.domain.api.SearchTrackRepository
import com.example.playlistmaker.domain.model.Track
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