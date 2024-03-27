package com.example.playlistmaker.search.ui

import android.os.Handler
import android.os.Looper
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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SearchViewModel(
    private val getTrackSearchQueryUseCase: GetTracksSearchQueryUseCase,
    private val clearTrackHistoryUseCase: ClearTrackHistoryUseCase,
    private val saveTrackToHistoryUseCase: SaveTrackToHistoryUseCase,
    private val getTrackHistoryUseCase: GetTrackHistoryUseCase,
    private val saveTrackToCacheUseCase: SaveTrackToCacheUseCase,
) : ViewModel() {

    private val userTextLiveData = MutableLiveData(DEFAULT_TEXT)
    private val searchStateLiveData = MutableLiveData<SearchState>(SearchState.Loading)

    fun getSearchStateLiveData(): LiveData<SearchState> = searchStateLiveData

    fun saveTrackToHistory(track: Track) {
        saveTrackToHistoryUseCase.execute(track)
    }

    fun updateUserText(text: String) {
        if (userTextLiveData.value != text) {
            userTextLiveData.value = text
        }
    }

    fun searchForTrack() {
        val text = userTextLiveData.value ?: return
        searchStateLiveData.value = SearchState.Loading
        viewModelScope.launch(Dispatchers.IO) {
            val tracks = getTrackSearchQueryUseCase.execute(text)
            withContext(Dispatchers.Main) {
                if (tracks != null) {
                    searchStateLiveData.value = SearchState.Content(tracks)
                } else {
                    searchStateLiveData.value = SearchState.Error
                }
            }
        }
    }

    fun loadHistoryData() {
        val tracks = getTrackHistoryUseCase.execute()
        searchStateLiveData.value = SearchState.History(tracks)
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
    private val handler = Handler(Looper.getMainLooper())
    private fun clickDebounce(): Boolean {
        val current = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false
            handler.postDelayed({ isClickAllowed = true }, SearchFragment.CLICK_DEBOUNCE_DELAY)
        }
        return current
    }
    fun searchDebounce() {
        handler.removeCallbacks(searchRunnable)
        handler.postDelayed(searchRunnable, SearchFragment.CLICK_DEBOUNCE_DELAY)
    }

    companion object {
        const val DEFAULT_TEXT = ""
    }
}