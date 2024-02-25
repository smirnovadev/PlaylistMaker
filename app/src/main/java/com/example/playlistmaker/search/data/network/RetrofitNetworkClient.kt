package com.example.playlistmaker.search.data.network

import com.example.playlistmaker.search.data.NetworkClient
import com.example.playlistmaker.search.data.dto.Response
import com.example.playlistmaker.search.data.dto.TrackSearchRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber

class RetrofitNetworkClient(private val itunesService: ItunesApi) : NetworkClient {

    override suspend fun doRequest(dto: Any): Response {
        if (dto !is TrackSearchRequest) {
            return Response().apply { resultCode = -1 }
        }

        return withContext(Dispatchers.IO) {
            try {
                val response = itunesService.search(dto.query)
                response.apply { resultCode = 200 }
            } catch (exception: Exception) {
                Timber.e(exception.message ?: "Unknown error")
                Response().apply { resultCode = 500 }
            }
        }
    }
}
