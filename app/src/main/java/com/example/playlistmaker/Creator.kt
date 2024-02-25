package com.example.playlistmaker

import android.annotation.SuppressLint
import android.content.Context
import com.example.playlistmaker.data.SearchTrackRepositoryImpl
import com.example.playlistmaker.data.TrackCacheRepositoryImpl
import com.example.playlistmaker.data.TrackHistoryRepositoryImpl
import com.example.playlistmaker.data.mapper.TrackMapper
import com.example.playlistmaker.data.network.RetrofitNetworkClient
import com.example.playlistmaker.domain.api.ClearTrackHistoryUseCase
import com.example.playlistmaker.domain.api.GetTrackFromCacheUseCase
import com.example.playlistmaker.domain.api.GetTrackHistoryUseCase
import com.example.playlistmaker.domain.api.GetTracksSearchQueryUseCase
import com.example.playlistmaker.domain.api.SaveTrackToCacheUseCase
import com.example.playlistmaker.domain.api.SaveTrackToHistoryUseCase
import com.example.playlistmaker.domain.impl.ClearTrackHistoryUseCaseImpl
import com.example.playlistmaker.domain.impl.GetTrackFromCacheUseCaseImpl
import com.example.playlistmaker.domain.impl.GetTrackHistoryUseCaseImpl
import com.example.playlistmaker.domain.impl.GetTracksSearchQueryUseCaseImpl
import com.example.playlistmaker.domain.impl.SaveTrackToCacheUseCaseImpl
import com.example.playlistmaker.domain.impl.SaveTrackToHistoryUseCaseImpl

@SuppressLint("StaticFieldLeak")
object Creator {

    private var context: Context? = null

    private val searchTrackRepository =
        SearchTrackRepositoryImpl(RetrofitNetworkClient(), TrackMapper())

    private val searchHistoryRepository by lazy {
        context?.let {
            TrackHistoryRepositoryImpl(it)
        } ?: error("context is not initialized")
    }
    private val trackRepository = TrackCacheRepositoryImpl()

    fun provideGetTrackSearchQueryUseCase(): GetTracksSearchQueryUseCase {
        return GetTracksSearchQueryUseCaseImpl(searchTrackRepository)
    }

    fun provideSaveTrackToHistoryUseCase(): SaveTrackToHistoryUseCase {
        return SaveTrackToHistoryUseCaseImpl(searchHistoryRepository)
    }

    fun provideGetTrackHistoryUseCase(): GetTrackHistoryUseCase {
        return GetTrackHistoryUseCaseImpl(searchHistoryRepository)
    }

    fun provideClearTrackHistoryUseCase(): ClearTrackHistoryUseCase {
        return ClearTrackHistoryUseCaseImpl(searchHistoryRepository)
    }

    fun provideGetTrackFromCacheUseCase(): GetTrackFromCacheUseCase {
        return GetTrackFromCacheUseCaseImpl(trackRepository)
    }
    fun provideSaveTrackToCacheUseCase(): SaveTrackToCacheUseCase {
        return SaveTrackToCacheUseCaseImpl(trackRepository)
    }

    fun init(context: Context) {
        this.context = context
    }
}