package com.example.playlistmaker.search.data.network

import android.util.Log
import com.example.playlistmaker.search.data.NetworkClient
import com.example.playlistmaker.search.data.dto.Response
import com.example.playlistmaker.search.data.dto.TrackSearchRequest
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitNetworkClient: NetworkClient {
    private val baseUrl = "https://itunes.apple.com"
    private val retrofit = Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    private val itunesService = retrofit.create(ItunesApi::class.java)
    override fun doRequest(dto: Any): Response {
        return if (dto is TrackSearchRequest) {
            val retrofitResponse = try {
                itunesService.search(dto.query).execute()
            } catch (exception: Exception) {
                Log.e("NetworkError", exception.message.toString())
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

