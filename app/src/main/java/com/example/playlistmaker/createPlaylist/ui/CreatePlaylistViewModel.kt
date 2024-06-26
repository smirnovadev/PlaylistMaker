package com.example.playlistmaker.createPlaylist.ui

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.createPlaylist.domain.SaveImageToPrivateStorageUseCase
import com.example.playlistmaker.createPlaylist.domain.model.DomainImage
import com.example.playlistmaker.createPlaylist.domain.model.Playlist
import com.example.playlistmaker.db.entity.domain.GetPlaylistByIdUseCase
import com.example.playlistmaker.db.entity.domain.SavePlaylistUseCase
import kotlinx.coroutines.launch

class CreatePlaylistViewModel(
    private val savePlaylistUseCase: SavePlaylistUseCase,
    private val saveImageToPrivateStorageUseCase: SaveImageToPrivateStorageUseCase,
    private val getPlaylistByIdUseCase: GetPlaylistByIdUseCase,
) : ViewModel() {

    private val coverArtPathLiveData: MutableLiveData<String> = MutableLiveData()
    private val nameLiveData: MutableLiveData<String> = MutableLiveData()
    private val descriptionLiveData: MutableLiveData<String> = MutableLiveData()
    private val playlistLiveData = MutableLiveData<Playlist>()

    fun getPlaylistLiveData(): LiveData<Playlist> {
        return playlistLiveData
    }

    fun saveCoverArt(uri: Uri) {
        viewModelScope.launch {
            val domainImage = DomainImage(uri.toString())
            val coverArtPath = saveImageToPrivateStorageUseCase.execute(domainImage)
            coverArtPathLiveData.value = coverArtPath
        }
    }

    fun createPlaylist() {
        val name = nameLiveData.value ?: return
        val description = descriptionLiveData.value ?: ""
        val coverArtPath = coverArtPathLiveData.value ?: ""

        val newPlaylist = Playlist(
            coverArtPath = coverArtPath,
            playlistName = name,
            description = description,
            trackIdList = emptyList(),
            numberTracks = 0
        )
        viewModelScope.launch { savePlaylistUseCase.execute(newPlaylist) }
    }

    fun updatePlaylist(playlist: Playlist) {
        val name = nameLiveData.value ?: return
        val description = descriptionLiveData.value ?: ""
        val coverArtPath = coverArtPathLiveData.value ?: ""

        val newPlaylist = playlist.copy(
            playlistName = name,
            description = description,
            coverArtPath = coverArtPath
        )
        viewModelScope.launch { savePlaylistUseCase.execute(newPlaylist) }
    }

    fun loadPlaylist(playlistId: Long) {
        viewModelScope.launch {
            getPlaylistByIdUseCase.execute(playlistId).collect { playlist ->
                playlistLiveData.value = playlist
                coverArtPathLiveData.value = playlist.coverArtPath
            }
        }

    }

    fun updateName(name: String) {
        nameLiveData.value = name
    }

    fun updateDescription(description: String) {
        descriptionLiveData.value = description
    }
}

