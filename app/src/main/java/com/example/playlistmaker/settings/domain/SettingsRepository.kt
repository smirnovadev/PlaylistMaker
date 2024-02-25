package com.example.playlistmaker.settings.domain

interface SettingsRepository {
    fun saveIsNightTheme(isNightTheme: Boolean)

    fun getIsNightTheme(): Boolean
}