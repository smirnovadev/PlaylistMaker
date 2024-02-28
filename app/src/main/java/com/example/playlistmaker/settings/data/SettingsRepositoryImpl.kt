package com.example.playlistmaker.settings.data

import android.content.Context
import com.example.playlistmaker.settings.domain.SettingsRepository

class SettingsRepositoryImpl(context: Context): SettingsRepository {

    private val sharedPref = context.getSharedPreferences(
        SETTINGS_SHARED_PREF, Context.MODE_PRIVATE
    )
    override fun saveIsNightTheme(isNightTheme: Boolean) {
        sharedPref.edit()
            .putBoolean(THEME_KEY, isNightTheme)
            .apply()
    }

    override fun getIsNightTheme(): Boolean {
        return sharedPref.getBoolean(THEME_KEY, false)
    }

    companion object{
        const val SETTINGS_SHARED_PREF = "SETTINGS_SHARED_PREF"
        const val THEME_KEY = "THEME_KEY"
    }
}