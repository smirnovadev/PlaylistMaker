package com.example.playlistmaker

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate

class App : Application() {

    var darkTheme = false

    override fun onCreate() {
        super.onCreate()

        val sharedPreferences = getSharedPreferences(MY_APP_SHARED_PREF, MODE_PRIVATE) ?: null
        if (sharedPreferences == null) {
            setTheme(R.style.Theme_PlaylistMaker)
        }

        Creator.init(applicationContext)

    }

    fun switchTheme(darkThemeEnabled: Boolean) {
        darkTheme = darkThemeEnabled
        AppCompatDelegate.setDefaultNightMode(
            if (darkThemeEnabled) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )
    }

    companion object{
        const val MY_APP_SHARED_PREF = "MY_APP_SHARED_PREF"
    }
}

