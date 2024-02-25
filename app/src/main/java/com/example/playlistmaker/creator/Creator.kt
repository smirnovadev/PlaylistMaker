package com.example.playlistmaker.creator

import android.annotation.SuppressLint
import android.content.Context
import com.example.playlistmaker.player.data.TrackCacheRepositoryImpl
import com.example.playlistmaker.player.domain.GetTrackFromCacheUseCase
import com.example.playlistmaker.player.domain.GetTrackFromCacheUseCaseImpl
import com.example.playlistmaker.search.data.SearchTrackRepositoryImpl
import com.example.playlistmaker.search.data.TrackHistoryRepositoryImpl
import com.example.playlistmaker.search.data.mapper.TrackMapper
import com.example.playlistmaker.search.data.network.RetrofitNetworkClient
import com.example.playlistmaker.search.domain.api.ClearTrackHistoryUseCase
import com.example.playlistmaker.search.domain.api.GetTrackHistoryUseCase
import com.example.playlistmaker.search.domain.api.GetTracksSearchQueryUseCase
import com.example.playlistmaker.search.domain.api.SaveTrackToCacheUseCase
import com.example.playlistmaker.search.domain.api.SaveTrackToHistoryUseCase
import com.example.playlistmaker.search.domain.impl.ClearTrackHistoryUseCaseImpl
import com.example.playlistmaker.search.domain.impl.GetTrackHistoryUseCaseImpl
import com.example.playlistmaker.search.domain.impl.GetTracksSearchQueryUseCaseImpl
import com.example.playlistmaker.search.domain.impl.SaveTrackToCacheUseCaseImpl
import com.example.playlistmaker.search.domain.impl.SaveTrackToHistoryUseCaseImpl
import com.example.playlistmaker.settings.data.SettingsRepositoryImpl
import com.example.playlistmaker.settings.domain.GetIsNightThemeUseCase
import com.example.playlistmaker.settings.domain.GetIsNightThemeUseCaseImpl
import com.example.playlistmaker.settings.domain.SaveThemeUseCase
import com.example.playlistmaker.settings.domain.SaveThemeUseCaseImpl

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

    private val settingsRepository by lazy {
        context?.let {
            SettingsRepositoryImpl(it)
        } ?: error("context is not initialized")
    }

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
        Creator.context = context
    }

    fun provideSaveThemeUseCase(): SaveThemeUseCase {
        return SaveThemeUseCaseImpl(settingsRepository)
    }

    fun provideGetIsNightThemeUseCase(): GetIsNightThemeUseCase {
        return GetIsNightThemeUseCaseImpl(settingsRepository)
    }
}