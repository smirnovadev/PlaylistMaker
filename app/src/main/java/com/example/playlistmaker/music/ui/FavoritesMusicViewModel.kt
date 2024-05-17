package com.example.playlistmaker.music.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.db.entity.domain.GetAllFavoriteTrackUseCase
import com.example.playlistmaker.search.domain.api.SaveTrackToCacheUseCase
import com.example.playlistmaker.search.domain.model.Track
import kotlinx.coroutines.launch

class FavoritesMusicViewModel(
    private val getAllFavoriteTrackUseCase: GetAllFavoriteTrackUseCase,
    private val saveTrackToCacheUseCase: SaveTrackToCacheUseCase
) : ViewModel() {
    private var favoriteTrackLiveData = MutableLiveData<FavoriteState>()
    fun getFavoriteTrackLiveData(): LiveData<FavoriteState> = favoriteTrackLiveData


    fun getAllFavoriteTrack() {
        viewModelScope.launch {
            getAllFavoriteTrackUseCase.execute()
                .collect { favoriteTracks ->
                    if (favoriteTracks.isEmpty()) {
                        favoriteTrackLiveData.postValue(FavoriteState.Empty)
                    } else {
                        favoriteTrackLiveData.postValue(FavoriteState.Content(favoriteTracks))
                    }
                }
        }
    }

    fun saveTrackToCache(trackData: Track) {
        saveTrackToCacheUseCase.execute(trackData)
    }
}
