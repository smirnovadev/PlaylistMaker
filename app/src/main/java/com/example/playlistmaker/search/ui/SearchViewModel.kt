package com.example.playlistmaker.search.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.search.domain.api.ClearTrackHistoryUseCase
import com.example.playlistmaker.search.domain.api.GetTrackHistoryUseCase
import com.example.playlistmaker.search.domain.api.GetTracksSearchQueryUseCase
import com.example.playlistmaker.search.domain.api.SaveTrackToCacheUseCase
import com.example.playlistmaker.search.domain.api.SaveTrackToHistoryUseCase
import com.example.playlistmaker.search.domain.model.Track
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class SearchViewModel(
    private val getTrackSearchQueryUseCase: GetTracksSearchQueryUseCase,
    private val clearTrackHistoryUseCase: ClearTrackHistoryUseCase,
    private val saveTrackToHistoryUseCase: SaveTrackToHistoryUseCase,
    private val getTrackHistoryUseCase: GetTrackHistoryUseCase,
    private val saveTrackToCacheUseCase: SaveTrackToCacheUseCase,
) : ViewModel() {
    private var searchJob: Job? = null
    private val userTextLiveData = MutableLiveData(DEFAULT_TEXT)
    private val searchStateLiveData = MutableLiveData<SearchState>(SearchState.Loading)

    fun getSearchStateLiveData(): LiveData<SearchState> = searchStateLiveData

    fun saveTrackToHistory(track: Track) {
        viewModelScope.launch { saveTrackToHistoryUseCase.execute(track).collect() }
    }

    fun updateUserText(text: String) {
        if (userTextLiveData.value != text) {
            userTextLiveData.value = text
        }
    }

    fun searchForTrack() {
        val text = userTextLiveData.value ?: return
        searchStateLiveData.value = SearchState.Loading
        viewModelScope.launch {
            getTrackSearchQueryUseCase.execute(text)
                .collect { tracks ->
                    searchStateLiveData.value = if (tracks != null) {
                        if (tracks.isEmpty()) {
                            SearchState.Empty
                        } else {
                            SearchState.Content(tracks)
                        }
                    } else {
                        SearchState.Error
                    }
                }
        }
    }

    fun loadHistoryData() {
        viewModelScope.launch {
            getTrackHistoryUseCase.execute().collect { tracks ->
                searchStateLiveData.value = SearchState.History(tracks)
            }
        }

    }

    fun saveTrackToCache(trackData: Track) {
        saveTrackToCacheUseCase.execute(trackData)
    }

    fun clearHistory() {
        clearTrackHistoryUseCase.execute()
    }

    private val searchRunnable = Runnable {
        if (clickDebounce() && userTextLiveData.value?.isNotEmpty() == true) {
            searchForTrack()
        }
    }

    private var isClickAllowed = true
    private fun clickDebounce(): Boolean {
        val current = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false
            viewModelScope.launch {
                delay(CLICK_DEBOUNCE_DELAY)
                isClickAllowed = true
            }
        }
        return current
    }

    fun searchDebounce() {
        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            delay(CLICK_DEBOUNCE_DELAY)
            searchRunnable.run()
        }
    }

    companion object {
        const val DEFAULT_TEXT = ""
        const val CLICK_DEBOUNCE_DELAY = 2000L
    }
}