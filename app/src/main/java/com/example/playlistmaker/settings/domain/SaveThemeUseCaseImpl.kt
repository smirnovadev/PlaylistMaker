package com.example.playlistmaker.settings.domain

class SaveThemeUseCaseImpl(private val settingsRepository: SettingsRepository): SaveThemeUseCase {
    override fun execute(isNightTheme: Boolean) {
        settingsRepository.saveIsNightTheme(isNightTheme)
    }
}