package com.example.playlistmaker.playlist.ui

import android.net.Uri
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.db.entity.domain.SavePlaylistUseCase
import com.example.playlistmaker.playlist.domain.SaveImageToPrivateStorageUseCase
import com.example.playlistmaker.playlist.domain.model.Playlist
import kotlinx.coroutines.launch

class CreatePlaylistViewModel(
    private val savePlaylistUseCase: SavePlaylistUseCase,
    private val saveImageToPrivateStorageUseCase: SaveImageToPrivateStorageUseCase
) : ViewModel() {

    private val coverArtPathLiveData: MutableLiveData<String> = MutableLiveData()
    private val nameLiveData: MutableLiveData<String> = MutableLiveData()
    private val descriptionLiveData: MutableLiveData<String> = MutableLiveData()

    fun saveCoverArt(uri: Uri) {
        viewModelScope.launch {
            val coverArtPath = saveImageToPrivateStorageUseCase.execute(uri)
            coverArtPathLiveData.value = coverArtPath
        }
    }

    fun savePlaylist() {
        val name = nameLiveData.value ?: return
        val description = descriptionLiveData.value ?: ""
        val coverArtPath = coverArtPathLiveData.value ?: ""

        val newPlaylist = Playlist(
            coverArtPath = coverArtPath,
            playlistName = name,
            description = description,
            tracksId = emptyList(),
            numberTracks = 0
        )
        viewModelScope.launch { savePlaylistUseCase.execute(newPlaylist) }
    }

    fun updateName(name: String) {
        nameLiveData.value = name
    }
    fun updateDescription(description: String) {
        descriptionLiveData.value = description
    }
}

