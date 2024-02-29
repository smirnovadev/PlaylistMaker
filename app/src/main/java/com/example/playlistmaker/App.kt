package com.example.playlistmaker

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import com.example.playlistmaker.settings.domain.GetIsNightThemeUseCase
import dataModule
import org.koin.android.BuildConfig
import org.koin.android.ext.android.inject
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level
import repositoryModule
import timber.log.Timber
import useCaseModule
import viewModel


class App : Application() {

    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }

        startKoin{
            androidLogger(Level.ERROR) // Use ERROR level to see only errors
            androidContext(this@App)
            modules(listOf(dataModule, repositoryModule, useCaseModule, viewModel))
        }
        val getIsNightThemeUseCase: GetIsNightThemeUseCase by inject()
        val isNightTheme = getIsNightThemeUseCase.execute()
        switchTheme(isNightTheme)
    }

    fun switchTheme(darkThemeEnabled: Boolean) {
        AppCompatDelegate.setDefaultNightMode(
            if (darkThemeEnabled) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )
    }
}

