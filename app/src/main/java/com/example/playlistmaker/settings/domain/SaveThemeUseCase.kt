package com.example.playlistmaker.settings.domain

interface SaveThemeUseCase {
    fun execute(isNightTheme: Boolean)
}