package com.example.playlistmaker.settings.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.playlistmaker.settings.domain.GetIsNightThemeUseCase
import com.example.playlistmaker.settings.domain.SaveThemeUseCase

class SettingsViewModel(
    private val saveThemeUseCase: SaveThemeUseCase,
    private val getThemeUseCase: GetIsNightThemeUseCase
) : ViewModel() {

    private val isNightThemeLiveData = MutableLiveData<Boolean>(false)
    init {
        isNightThemeLiveData.value = getThemeUseCase.execute()
    }

     fun getIsNightThemeLiveData(): LiveData<Boolean> = isNightThemeLiveData

    fun saveTheme(isNightTheme: Boolean) {
        isNightThemeLiveData.value = isNightTheme
        saveThemeUseCase.execute(isNightTheme)
    }
}