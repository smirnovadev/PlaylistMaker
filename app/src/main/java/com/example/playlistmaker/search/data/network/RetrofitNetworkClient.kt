package com.example.playlistmaker.search.data.network

import com.example.playlistmaker.search.data.NetworkClient
import com.example.playlistmaker.search.data.dto.Response
import com.example.playlistmaker.search.data.dto.TrackSearchRequest
import timber.log.Timber

class RetrofitNetworkClient(private val itunesService: ItunesApi): NetworkClient {

    override fun doRequest(dto: Any): Response {
        return if (dto is TrackSearchRequest) {
            val retrofitResponse = try {
                itunesService.search(dto.query).execute()
            } catch (exception: Exception) {
                Timber.e(exception.message ?: "Unknown error")
                return Response()
            }

            val response = retrofitResponse.body() ?: Response()
            response.resultCode = retrofitResponse.code()
            response
        } else {
            Response().apply { resultCode = 400 }
        }
    }
}

