package com.example.playlistmaker.music.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.db.entity.domain.GetAllPlaylistUseCase
import kotlinx.coroutines.launch

class AllPlaylistsViewModel(
    private val getAllPlaylistUseCase: GetAllPlaylistUseCase
) : ViewModel() {
    private var playlistTrackLiveData = MutableLiveData<AllPlaylistsState>()
    fun getPlaylistTrackLiveData(): LiveData<AllPlaylistsState> = playlistTrackLiveData


    fun getAllPlaylist() {
        viewModelScope.launch {
            getAllPlaylistUseCase.execute()
                .collect { playlist ->
                    if (playlist.isEmpty()) {
                        playlistTrackLiveData.postValue(AllPlaylistsState.Empty)
                    } else {
                        playlistTrackLiveData.postValue(AllPlaylistsState.Content(playlist))
                    } }
        }
    }
}