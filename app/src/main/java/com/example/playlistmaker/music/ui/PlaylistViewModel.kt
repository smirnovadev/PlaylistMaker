package com.example.playlistmaker.music.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.db.entity.domain.GetAllPlaylistUseCase
import kotlinx.coroutines.launch

class PlaylistViewModel(
    private val getAllPlaylistUseCase: GetAllPlaylistUseCase
) : ViewModel() {
    private var playlistTrackLiveData = MutableLiveData<PlaylistState>()
    fun getPlaylistTrackLiveData(): LiveData<PlaylistState> = playlistTrackLiveData


    fun getAllPlaylist() {
        viewModelScope.launch {
            getAllPlaylistUseCase.execute()
                .collect { playlist ->
                    if (playlist.isEmpty()) {
                        playlistTrackLiveData.postValue(PlaylistState.Empty)
                    } else {
                        playlistTrackLiveData.postValue(PlaylistState.Content(playlist))
                    } }
        }
    }
}