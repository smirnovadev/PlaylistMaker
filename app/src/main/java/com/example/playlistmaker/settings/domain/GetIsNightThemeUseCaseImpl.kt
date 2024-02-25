package com.example.playlistmaker.settings.domain

class GetIsNightThemeUseCaseImpl(private val settingsRepository: SettingsRepository): GetIsNightThemeUseCase {
    override fun execute(): Boolean {
        return settingsRepository.getIsNightTheme()
    }
}