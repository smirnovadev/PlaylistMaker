package com.example.playlistmaker.settings.data

import android.content.SharedPreferences
import com.example.playlistmaker.settings.domain.SettingsRepository

class SettingsRepositoryImpl(private val sharedPref: SharedPreferences): SettingsRepository {

    override fun saveIsNightTheme(isNightTheme: Boolean) {
        sharedPref.edit()
            .putBoolean(THEME_KEY, isNightTheme)
            .apply()
    }

    override fun getIsNightTheme(): Boolean {
        return sharedPref.getBoolean(THEME_KEY, false)
    }

    companion object{
        const val THEME_KEY = "THEME_KEY"
    }
}